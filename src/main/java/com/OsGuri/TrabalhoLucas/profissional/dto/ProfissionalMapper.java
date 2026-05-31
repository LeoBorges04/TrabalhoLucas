package com.OsGuri.TrabalhoLucas.profissional.dto;

import com.OsGuri.TrabalhoLucas.especialidade.EspecialidadeEntity;
import com.OsGuri.TrabalhoLucas.especialidade.dto.EspecialidadeMapper;
import com.OsGuri.TrabalhoLucas.profissional.ProfissionalEntity;
import org.springframework.stereotype.Component;

@Component
public class ProfissionalMapper {

    private final EspecialidadeMapper especialidadeMapper;

    public ProfissionalMapper(EspecialidadeMapper especialidadeMapper) {
        this.especialidadeMapper = especialidadeMapper;
    }

    public ProfissionalEntity map(ProfissionalRequestDto dto, EspecialidadeEntity especialidade) {
        ProfissionalEntity entidade = new ProfissionalEntity();
        entidade.setNome(dto.getNome());
        entidade.setRegistroConselho(dto.getRegistroConselho());
        entidade.setEspecialidade(especialidade);
        entidade.setCargo(dto.getCargo());
        entidade.setTurno(dto.getTurno());
        entidade.setTelefone(dto.getTelefone());
        entidade.setEmail(dto.getEmail());
        entidade.setAtivo(true);
        return entidade;
    }

    public ProfissionalResponseDto map(ProfissionalEntity entidade) {
        ProfissionalResponseDto dto = new ProfissionalResponseDto();
        dto.setId(entidade.getId());
        dto.setAtivo(entidade.isAtivo());
        dto.setNome(entidade.getNome());
        dto.setRegistroConselho(entidade.getRegistroConselho());
        if (entidade.getEspecialidade() != null) {
            dto.setEspecialidade(especialidadeMapper.map(entidade.getEspecialidade()));
        }
        dto.setCargo(entidade.getCargo());
        dto.setTurno(entidade.getTurno());
        dto.setTelefone(entidade.getTelefone());
        dto.setEmail(entidade.getEmail());
        return dto;
    }
}
