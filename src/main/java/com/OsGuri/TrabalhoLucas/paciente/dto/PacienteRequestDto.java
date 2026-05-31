package com.OsGuri.TrabalhoLucas.paciente.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data
public class PacienteRequestDto {

    @NotBlank(
            message = "campo nome é obrigatório"
    )
    private String nomeCompleto;

    @NotBlank(
            message = "campo CPF é obrigatório"
    )
    @CPF(
            message = "campo CPF deve ser válido"
    )
    private String cpf;

    @NotBlank(
            message = "campo sexo é obrigatório"
    )
    private String sexo;
    @NotBlank(
            message = "campo telefone é obrigatório"
    )
    @Pattern(
            regexp = "\\d{10,11}",
            message = "telefone deve conter 10 ou 11 digitos numéricos"
    )
    private String telefone;

    @NotBlank(
            message = "campo email é obrigatório"
    )
    @Email(
            message = "campo email deve ser válido"
    )
    private String email;

    @NotBlank(
            message = "campo endereço é obrigatório"
    )
    private String endereco;

    @NotNull(
            message = "campo dataNascimento é obrigatório"
    )
    @Past(
            message = "a data deve estar no passado"
    )
    private LocalDate dataNascimento;

    @NotBlank(
            message= "campo numeroCarteirinha é obrigatório"
    )
    private String numeroCarteirinha;

    @NotBlank(
            message =   "campo convenio é obrigatório"
    )
    private String convenio;
}
