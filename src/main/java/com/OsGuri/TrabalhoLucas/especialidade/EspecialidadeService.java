package com.OsGuri.TrabalhoLucas.especialidade;

import com.OsGuri.TrabalhoLucas.especialidade.dto.*;
import com.OsGuri.TrabalhoLucas.exception.ConflitoException;
import com.OsGuri.TrabalhoLucas.exception.RecursoNaoEncontradoException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EspecialidadeService {
    private final EspecialidadeRepository especialidadeRepository;
    private final EspecialidadeMapper especialidadeMapper;

    public EspecialidadeService(EspecialidadeRepository especialidadeRepository, EspecialidadeMapper especialidadeMapper) {
        this.especialidadeRepository = especialidadeRepository;
        this.especialidadeMapper = especialidadeMapper;
    }

    @Transactional
    public EspecialidadeResponseDto cadastrar(EspecialidadeRequestDto dto) {
        if (especialidadeRepository.existsByNome(dto.getNome())) {
            throw new ConflitoException("Especialidade com este nome já cadastrada");
        }
        EspecialidadeEntity entidade = especialidadeMapper.map(dto);
        EspecialidadeEntity salva = especialidadeRepository.save(entidade);
        return especialidadeMapper.map(salva);
    }

    public EspecialidadeResponseDto listar(Long id) {
        EspecialidadeEntity entidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Especialidade não encontrada"));
        if (!entidade.isAtivo()) {
            throw new ConflitoException("Especialidade está inativa");
        }
        return especialidadeMapper.map(entidade);
    }

    public List<EspecialidadeResponseDto> listarRegistros() {
        return especialidadeRepository.findAll()
                .stream()
                .map(especialidadeMapper::map)
                .toList();
    }

    public List<EspecialidadeResponseDto> listarAtivos() {
        return especialidadeRepository.findAllByAtivoTrue()
                .stream()
                .map(especialidadeMapper::map)
                .toList();
    }

    @Transactional
    public EspecialidadeResponseDto atualizarParcial(Long id, EspecialidadePatchDto dto) {
        EspecialidadeEntity entidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Especialidade não encontrada"));
        
        if (!entidade.isAtivo()) {
            throw new ConflitoException("Especialidade está inativa");
        }

        if (dto.getNome() != null && !dto.getNome().equals(entidade.getNome())) {
            if (especialidadeRepository.existsByNome(dto.getNome())) {
                throw new ConflitoException("Especialidade com este nome já cadastrada");
            }
            entidade.setNome(dto.getNome());
        }

        if (dto.getDescricao() != null) {
            entidade.setDescricao(dto.getDescricao());
        }

        if (dto.getArea() != null) {
            entidade.setArea(dto.getArea());
        }

        especialidadeRepository.save(entidade);
        return especialidadeMapper.map(entidade);
    }

    @Transactional
    public void deletar(Long id) {
        EspecialidadeEntity entidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Especialidade não encontrada"));
        if (!entidade.isAtivo()) {
            throw new ConflitoException("Especialidade já está inativa");
        }
        entidade.setAtivo(false);
    }

    @Transactional
    public EspecialidadeResponseDto reativar(Long id) {
        EspecialidadeEntity entidade = especialidadeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Especialidade não encontrada"));
        if (entidade.isAtivo()) {
            throw new ConflitoException("Especialidade já está ativa");
        }
        entidade.setAtivo(true);
        return especialidadeMapper.map(entidade);
    }
}
