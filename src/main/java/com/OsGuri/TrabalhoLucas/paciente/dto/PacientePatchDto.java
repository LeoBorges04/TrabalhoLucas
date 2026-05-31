package com.OsGuri.TrabalhoLucas.paciente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data

public class PacientePatchDto {

    private String nome;
    @CPF(message = "CPF inválido")
    private String cpf;
    @Pattern(
            regexp = "\\d{10,11}",
            message = "Telefone deve conter 10 ou 11 dígitos numéricos"
    )
    private String telefone;
    @Email(message = "Email inválido")
    private String email;
    private String endereco;
    @Past(message = "Data de nascimento deve ser no passado")
    private LocalDate data_nascimento;
    private String sexo;
    private String convenio;
    private String numeroCarteirinha;
}
