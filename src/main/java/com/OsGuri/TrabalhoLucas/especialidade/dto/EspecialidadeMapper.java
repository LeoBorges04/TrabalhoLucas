package com.OsGuri.TrabalhoLucas.especialidade.dto;

import com.OsGuri.TrabalhoLucas.especialidade.EspecialidadeEntity;
import org.springframework.stereotype.Component;

@Component
public class EspecialidadeMapper {

    public EspecialidadeEntity map(EspecialidadeRequestDto dto) {
        EspecialidadeEntity entidade = new EspecialidadeEntity();
        entidade.setNome(dto.getNome());
        entidade.setDescricao(dto.getDescricao());
        entidade.setArea(dto.getArea());
        entidade.setAtivo(true);
        return entidade;
    }

    public EspecialidadeResponseDto map(EspecialidadeEntity entidade) {
        EspecialidadeResponseDto dto = new EspecialidadeResponseDto();
        dto.setId(entidade.getId());
        dto.setAtivo(entidade.isAtivo());
        dto.setNome(entidade.getNome());
        dto.setDescricao(entidade.getDescricao());
        dto.setArea(entidade.getArea());
        return dto;
    }
}
