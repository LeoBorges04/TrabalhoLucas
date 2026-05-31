package com.OsGuri.TrabalhoLucas.profissional.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ProfissionalRequestDto {
    @NotBlank(message = "O campo nome é obrigatório")
    private String nome;

    @NotBlank(message = "O campo registroConselho é obrigatório")
    private String registroConselho;

    @NotNull(message = "O campo especialidadeId é obrigatório")
    private Long especialidadeId;

    @NotBlank(message = "O campo cargo é obrigatório")
    private String cargo;

    @NotBlank(message = "O campo turno é obrigatório")
    private String turno;

    @NotBlank(message = "O campo telefone é obrigatório")
    @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter 10 ou 11 dígitos numéricos")
    private String telefone;

    @NotBlank(message = "O campo email é obrigatório")
    @Email(message = "O campo email deve ser válido")
    private String email;
}
