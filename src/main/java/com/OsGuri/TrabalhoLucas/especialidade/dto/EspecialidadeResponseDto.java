package com.OsGuri.TrabalhoLucas.especialidade.dto;

import lombok.Data;

@Data
public class EspecialidadeResponseDto {
    private Long id;
    private boolean ativo;
    private String nome;
    private String descricao;
    private String area;
}
