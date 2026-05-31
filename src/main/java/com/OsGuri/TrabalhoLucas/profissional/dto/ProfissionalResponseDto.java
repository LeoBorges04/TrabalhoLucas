package com.OsGuri.TrabalhoLucas.profissional.dto;

import com.OsGuri.TrabalhoLucas.especialidade.dto.EspecialidadeResponseDto;
import lombok.Data;

@Data
public class ProfissionalResponseDto {
    private Long id;
    private boolean ativo;
    private String nome;
    private String registroConselho;
    private EspecialidadeResponseDto especialidade;
    private String cargo;
    private String turno;
    private String telefone;
    private String email;
}
