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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfissionalRepository profissionalRepository;
    private final AtendimentoMapper atendimentoMapper;

    public AtendimentoService(AtendimentoRepository atendimentoRepository,
                              PacienteRepository pacienteRepository,
                              ProfissionalRepository profissionalRepository,
                              AtendimentoMapper atendimentoMapper) {
        this.atendimentoRepository = atendimentoRepository;
        this.pacienteRepository = pacienteRepository;
        this.profissionalRepository = profissionalRepository;
        this.atendimentoMapper = atendimentoMapper;
    }

    public AtendimentoResponseDto create(AtendimentoRequestDto request) {
        PacienteEntity paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Paciente não encontrado com o ID fornecido: " + request.getPacienteId()));

        ProfissionalEntity profissional = profissionalRepository.findById(request.getProfissionalId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Profissional não encontrado com o ID fornecido: " + request.getProfissionalId()));

        AtendimentoEntity entidade = atendimentoMapper.map(request, paciente, profissional);
        entidade = atendimentoRepository.save(entidade);

        return atendimentoMapper.map(entidade);
    }

    public AtendimentoResponseDto update(Long id, AtendimentoPatchDto request) {
        AtendimentoEntity entidade = atendimentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atendimento não encontrado com o ID fornecido: " + id));

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
        return atendimentoMapper.map(entidade);
    }

    public List<AtendimentoResponseDto> findAll() {
        return atendimentoRepository.findAll().stream()
                .map(atendimentoMapper::map)
                .collect(Collectors.toList());
    }

    public AtendimentoResponseDto findById(Long id) {
        AtendimentoEntity entidade = atendimentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atendimento não encontrado com o ID fornecido: " + id));
        return atendimentoMapper.map(entidade);
    }

    public void delete(Long id) {
        AtendimentoEntity entidade = atendimentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atendimento não encontrado com o ID fornecido: " + id));
        atendimentoRepository.delete(entidade);
    }

    public List<AtendimentoResponseDto> findByData(LocalDateTime start, LocalDateTime end) {
        return atendimentoRepository.findByDataHoraBetween(start, end).stream()
                .map(atendimentoMapper::map)
                .collect(Collectors.toList());
    }
}
