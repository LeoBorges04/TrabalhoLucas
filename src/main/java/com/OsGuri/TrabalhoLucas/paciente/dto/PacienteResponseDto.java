package com.OsGuri.TrabalhoLucas.paciente.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PacienteResponseDto {
    private Long id;
    private boolean ativo;
    private String nomeCompleto;
    private String cpf;
    private LocalDate dataNascimento;
    private String telefone;
    private String endereco;
    private String sexo;
    private String email;
    private String convenio;
    private String numero_carteirinha;
}
