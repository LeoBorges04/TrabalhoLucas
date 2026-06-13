package com.OsGuri.TrabalhoLucas.atendimentos;

import com.OsGuri.TrabalhoLucas.atendimentos.dto.AtendimentoMapper;
import com.OsGuri.TrabalhoLucas.atendimentos.dto.AtendimentoPatchDto;
import com.OsGuri.TrabalhoLucas.atendimentos.dto.AtendimentoRequestDto;
import com.OsGuri.TrabalhoLucas.atendimentos.dto.AtendimentoResponseDto;
import com.OsGuri.TrabalhoLucas.exception.RecursoNaoEncontradoException;
import com.OsGuri.TrabalhoLucas.paciente.PacienteEntity;
import com.OsGuri.TrabalhoLucas.paciente.PacienteRepository;
import com.OsGuri.TrabalhoLucas.profissional.ProfissionalEntity;
import com.OsGuri.TrabalhoLucas.profissional.ProfissionalRepository;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.OsGuri.TrabalhoLucas.usuario.RoleEnum;
import com.OsGuri.TrabalhoLucas.usuario.UsuarioEntity;
import com.OsGuri.TrabalhoLucas.usuario.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfissionalRepository profissionalRepository;
    private final AtendimentoMapper atendimentoMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final UsuarioRepository usuarioRepository;

    public AtendimentoService(AtendimentoRepository atendimentoRepository,
                              PacienteRepository pacienteRepository,
                              ProfissionalRepository profissionalRepository,
                              AtendimentoMapper atendimentoMapper,
                              SimpMessagingTemplate messagingTemplate,
                              UsuarioRepository usuarioRepository) {
        this.atendimentoRepository = atendimentoRepository;
        this.pacienteRepository = pacienteRepository;
        this.profissionalRepository = profissionalRepository;
        this.atendimentoMapper = atendimentoMapper;
        this.messagingTemplate = messagingTemplate;
        this.usuarioRepository = usuarioRepository;
    }

    @jakarta.annotation.PostConstruct
    public void fixOldRecords() {
        List<AtendimentoEntity> oldRecords = atendimentoRepository.findAll();
        boolean changed = false;
        for (AtendimentoEntity a : oldRecords) {
            if (a.getVisivelPaciente() == null) {
                a.setVisivelPaciente(true);
                changed = true;
            }
            if (a.getVisivelProfissional() == null) {
                a.setVisivelProfissional(true);
                changed = true;
            }
        }
        if (changed) {
            atendimentoRepository.saveAll(oldRecords);
            System.out.println("Migração: Atualizado registros antigos com visibilidade NULL para true.");
        }
    }

    public AtendimentoResponseDto create(AtendimentoRequestDto request) {
        if (request.getPacienteId() == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated()) {
                String email = auth.getName();
                PacienteEntity p = pacienteRepository.findByEmail(email).orElseThrow(() -> new RecursoNaoEncontradoException("Paciente logado não encontrado"));
                request.setPacienteId(p.getId());
            }
        }

        PacienteEntity paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado com o ID fornecido: " + request.getPacienteId()));

        ProfissionalEntity profissional = null;
        if (request.getProfissionalId() != null) {
            profissional = profissionalRepository.findById(request.getProfissionalId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Profissional não encontrado com o ID fornecido: " + request.getProfissionalId()));
        }

        AtendimentoEntity entidade = atendimentoMapper.map(request, paciente, profissional);
        entidade = atendimentoRepository.save(entidade);

        AtendimentoResponseDto response = atendimentoMapper.map(entidade);
        
        // Emite o evento via WebSocket
        messagingTemplate.convertAndSend("/topic/solicitacoes", response);

        return response;
    }

    public AtendimentoResponseDto update(Long id, AtendimentoPatchDto request) {
        AtendimentoEntity entidade = atendimentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atendimento não encontrado com o ID fornecido: " + id));

        if (request.getProfissionalId() != null) {
            ProfissionalEntity profissional = profissionalRepository.findById(request.getProfissionalId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Profissional não encontrado com o ID fornecido: " + request.getProfissionalId()));
            entidade.setProfissional(profissional);
        }
        if (request.getEspecialidade() != null) {
            entidade.setEspecialidade(request.getEspecialidade());
        }
        if (request.getDataHora() != null) {
            entidade.setDataHora(request.getDataHora());
        }
        if (request.getTipo() != null) {
            entidade.setTipo(request.getTipo());
        }
        if (request.getStatus() != null) {
            entidade.setStatus(request.getStatus());
        }
        if (request.getDiagnostico() != null) {
            entidade.setDiagnostico(request.getDiagnostico());
        }
        if (request.getObservacoes() != null) {
            entidade.setObservacoes(request.getObservacoes());
        }
        if (request.getValor() != null) {
            entidade.setValor(request.getValor());
        }

        entidade = atendimentoRepository.save(entidade);
        AtendimentoResponseDto response = atendimentoMapper.map(entidade);
        
        // Broadcast updates
        messagingTemplate.convertAndSend("/topic/solicitacoes", response);
        
        return response;
    }

    public List<AtendimentoResponseDto> findAll() {
        return atendimentoRepository.findAllByAtivoTrue().stream()
                .map(atendimentoMapper::map)
                .collect(Collectors.toList());
    }

    public AtendimentoResponseDto findById(Long id) {
        AtendimentoEntity entidade = atendimentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atendimento não encontrado com o ID fornecido: " + id));
        if (!entidade.isAtivo()) {
            throw new RecursoNaoEncontradoException("Atendimento não encontrado com o ID fornecido: " + id);
        }
        return atendimentoMapper.map(entidade);
    }

    public void delete(Long id) {
        AtendimentoEntity entidade = atendimentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atendimento não encontrado com o ID fornecido: " + id));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String email = auth.getName();
            UsuarioEntity user = usuarioRepository.findByEmail(email).orElse(null);
            if (user != null) {
                System.out.println("Deletando atendimento " + id + " pelo usuario " + email + " com role " + user.getRole());
                if (user.getRole() == RoleEnum.PACIENTE) {
                    entidade.setVisivelPaciente(false);
                } else if (user.getRole() == RoleEnum.MEDICO) {
                    entidade.setVisivelProfissional(false);
                } else {
                    entidade.setAtivo(false);
                }
            } else {
                System.out.println("Usuario nulo ao deletar atendimento " + id);
                entidade.setAtivo(false);
            }
        } else {
            System.out.println("Auth nulo ao deletar atendimento " + id);
            entidade.setAtivo(false);
        }

        atendimentoRepository.save(entidade);
        System.out.println("Atendimento salvo: ativo=" + entidade.isAtivo() + ", visivelPaciente=" + entidade.getVisivelPaciente() + ", visivelProfissional=" + entidade.getVisivelProfissional());
        
        // Emite evento para os clientes removerem da tela
        AtendimentoResponseDto response = atendimentoMapper.map(entidade);
        messagingTemplate.convertAndSend("/topic/solicitacoes", response);
    }

    public List<AtendimentoResponseDto> findByData(LocalDateTime start, LocalDateTime end) {
        return atendimentoRepository.findByDataHoraBetweenAndAtivoTrue(start, end).stream()
                .map(atendimentoMapper::map)
                .collect(Collectors.toList());
    }

    public List<AtendimentoResponseDto> findMyAtendimentos() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado");
        }

        String email = auth.getName();
        UsuarioEntity user = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        
        if (user.getRole() == RoleEnum.PACIENTE) {
            PacienteEntity p = pacienteRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Paciente logado não encontrado"));
            return atendimentoRepository.findByPacienteIdAndVisivelPacienteTrueAndAtivoTrue(p.getId()).stream().map(atendimentoMapper::map).collect(Collectors.toList());
        } else if (user.getRole() == RoleEnum.MEDICO) {
            ProfissionalEntity prof = profissionalRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Médico logado não encontrado"));
            return atendimentoRepository.findByProfissionalIdAndVisivelProfissionalTrueAndAtivoTrue(prof.getId()).stream().map(atendimentoMapper::map).collect(Collectors.toList());
        } else {
            // ADMIN vê todos
            return findAll();
        }
    }
}
