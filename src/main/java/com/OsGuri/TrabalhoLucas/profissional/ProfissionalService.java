package com.OsGuri.TrabalhoLucas.profissional;

import com.OsGuri.TrabalhoLucas.especialidade.EspecialidadeEntity;
import com.OsGuri.TrabalhoLucas.especialidade.EspecialidadeRepository;
import com.OsGuri.TrabalhoLucas.exception.ConflitoException;
import com.OsGuri.TrabalhoLucas.exception.RecursoNaoEncontradoException;
import com.OsGuri.TrabalhoLucas.profissional.dto.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfissionalService {
    private final ProfissionalRepository profissionalRepository;
    private final ProfissionalMapper profissionalMapper;
    private final EspecialidadeRepository especialidadeRepository;

    public ProfissionalService(ProfissionalRepository profissionalRepository,
                               ProfissionalMapper profissionalMapper,
                               EspecialidadeRepository especialidadeRepository) {
        this.profissionalRepository = profissionalRepository;
        this.profissionalMapper = profissionalMapper;
        this.especialidadeRepository = especialidadeRepository;
    }

    @Transactional
    public ProfissionalResponseDto cadastrar(ProfissionalRequestDto dto) {
        if (profissionalRepository.existsByEmail(dto.getEmail())) {
            throw new ConflitoException("Já existe profissional cadastrado com esse email");
        }
        if (profissionalRepository.existsByRegistroConselho(dto.getRegistroConselho())) {
            throw new ConflitoException("Já existe profissional cadastrado com esse registro no conselho");
        }

        EspecialidadeEntity especialidade = especialidadeRepository.findById(dto.getEspecialidadeId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Especialidade não encontrada"));

        if (!especialidade.isAtivo()) {
            throw new ConflitoException("Especialidade está inativa");
        }

        ProfissionalEntity entidade = profissionalMapper.map(dto, especialidade);
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

        if (dto.getEspecialidadeId() != null && (entidade.getEspecialidade() == null || !entidade.getEspecialidade().getId().equals(dto.getEspecialidadeId()))) {
            EspecialidadeEntity especialidade = especialidadeRepository.findById(dto.getEspecialidadeId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Especialidade não encontrada"));
            if (!especialidade.isAtivo()) {
                throw new ConflitoException("Especialidade está inativa");
            }
            entidade.setEspecialidade(especialidade);
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
}
