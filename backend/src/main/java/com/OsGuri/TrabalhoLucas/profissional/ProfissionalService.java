package com.OsGuri.TrabalhoLucas.profissional;

import com.OsGuri.TrabalhoLucas.especialidade.EspecialidadeEnum;
import com.OsGuri.TrabalhoLucas.exception.ConflitoException;
import com.OsGuri.TrabalhoLucas.exception.RecursoNaoEncontradoException;
import com.OsGuri.TrabalhoLucas.profissional.dto.*;
import com.OsGuri.TrabalhoLucas.usuario.UsuarioEntity;
import com.OsGuri.TrabalhoLucas.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfissionalService {
    private final ProfissionalRepository profissionalRepository;
    private final ProfissionalMapper profissionalMapper;
    private final UsuarioRepository usuarioRepository;

    public ProfissionalService(ProfissionalRepository profissionalRepository,
                               ProfissionalMapper profissionalMapper,
                               UsuarioRepository usuarioRepository) {
        this.profissionalRepository = profissionalRepository;
        this.profissionalMapper = profissionalMapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public ProfissionalResponseDto cadastrar(ProfissionalRequestDto dto) {
        if (profissionalRepository.existsByEmail(dto.getEmail())) {
            throw new ConflitoException("Já existe profissional cadastrado com esse email");
        }
        if (profissionalRepository.existsByRegistroConselho(dto.getRegistroConselho())) {
            throw new ConflitoException("Já existe profissional cadastrado com esse registro no conselho");
        }

        ProfissionalEntity entidade = profissionalMapper.map(dto);
        ProfissionalEntity salva = profissionalRepository.save(entidade);
        return profissionalMapper.map(salva);
    }

    public ProfissionalResponseDto listar(Long id) {
        ProfissionalEntity entidade = profissionalRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Profissional não encontrado"));
        if (!entidade.isAtivo()) {
            throw new ConflitoException("Profissional está inativo");
        }
        return profissionalMapper.map(entidade);
    }

    public List<ProfissionalResponseDto> listarRegistros() {
        return profissionalRepository.findAll()
                .stream()
                .map(profissionalMapper::map)
                .toList();
    }

    public List<ProfissionalResponseDto> listarAtivos() {
        return profissionalRepository.findAllByAtivoTrue()
                .stream()
                .map(profissionalMapper::map)
                .toList();
    }

    public List<ProfissionalResponseDto> listarPorEspecialidade(EspecialidadeEnum especialidade) {
        return profissionalRepository.findByEspecialidadesContainingAndAtivoTrue(especialidade)
                .stream()
                .map(profissionalMapper::map)
                .toList();
    }

    @Transactional
    public ProfissionalResponseDto atualizarParcial(Long id, ProfissionalPatchDto dto) {
        ProfissionalEntity entidade = profissionalRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Profissional não encontrado"));
        
        if (!entidade.isAtivo()) {
            throw new ConflitoException("Profissional está inativo");
        }

        if (dto.getNome() != null) {
            entidade.setNome(dto.getNome());
        }

        if (dto.getRegistroConselho() != null && !dto.getRegistroConselho().equals(entidade.getRegistroConselho())) {
            if (profissionalRepository.existsByRegistroConselho(dto.getRegistroConselho())) {
                throw new ConflitoException("Já existe profissional cadastrado com esse registro no conselho");
            }
            entidade.setRegistroConselho(dto.getRegistroConselho());
        }

        if (dto.getEspecialidades() != null) {
            entidade.setEspecialidades(dto.getEspecialidades());
        }

        if (dto.getCargo() != null) {
            entidade.setCargo(dto.getCargo());
        }

        if (dto.getTurno() != null) {
            entidade.setTurno(dto.getTurno());
        }

        if (dto.getTelefone() != null) {
            entidade.setTelefone(dto.getTelefone());
        }

        if (dto.getEmail() != null && !dto.getEmail().equals(entidade.getEmail())) {
            if (profissionalRepository.existsByEmail(dto.getEmail())) {
                throw new ConflitoException("Já existe profissional cadastrado com esse email");
            }
            
            Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findByEmail(entidade.getEmail());
            if (usuarioOpt.isPresent()) {
                UsuarioEntity usuario = usuarioOpt.get();
                usuario.setEmail(dto.getEmail());
                usuarioRepository.save(usuario);
            }
            
            entidade.setEmail(dto.getEmail());
        }

        profissionalRepository.save(entidade);
        return profissionalMapper.map(entidade);
    }

    @Transactional
    public void deletar(Long id) {
        ProfissionalEntity entidade = profissionalRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Profissional não encontrado"));
        if (!entidade.isAtivo()) {
            throw new ConflitoException("Profissional já está inativo");
        }
        entidade.setAtivo(false);
    }

    @Transactional
    public ProfissionalResponseDto reativar(Long id) {
        ProfissionalEntity entidade = profissionalRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Profissional não encontrado"));
        if (entidade.isAtivo()) {
            throw new ConflitoException("Profissional já está ativo");
        }
        entidade.setAtivo(true);
        return profissionalMapper.map(entidade);
    }

    public ProfissionalResponseDto obterMeuPerfil() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado");
        }
        String email = auth.getName();
        ProfissionalEntity profissional = profissionalRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Profissional logado não encontrado"));
        return profissionalMapper.map(profissional);
    }
}
