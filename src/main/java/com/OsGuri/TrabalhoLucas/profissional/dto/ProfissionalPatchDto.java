package com.OsGuri.TrabalhoLucas.profissional.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ProfissionalPatchDto {
    private String nome;
    private String registroConselho;
    private Long especialidadeId;
    private String cargo;
    private String turno;
    @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter 10 ou 11 dígitos numéricos")
    private String telefone;
    @Email(message = "O campo email deve ser válido")
    private String email;
}
