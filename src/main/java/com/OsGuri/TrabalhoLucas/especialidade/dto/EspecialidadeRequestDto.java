package com.OsGuri.TrabalhoLucas.especialidade.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EspecialidadeRequestDto {

    @NotBlank(message = "O campo nome é obrigatório")
    private String nome;

    @NotBlank(message = "O campo descrição é obrigatório")
    private String descricao;

    @NotBlank(message = "O campo área é obrigatório")
    private String area;
}
