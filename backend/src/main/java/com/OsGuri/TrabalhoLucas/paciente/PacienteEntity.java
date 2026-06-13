package com.OsGuri.TrabalhoLucas.paciente;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "tb_supaciente")
public class PacienteEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private boolean ativo = true;
    @Column(
            nullable = false
    )
    private String nome;
    @Column(
            nullable = false,
            unique = true
    )
    private String cpf;
    @Column(
            nullable = false
    )
    private LocalDate dataNascimento;
    @Column(
            nullable = false
    )
    private String sexo;
    @Column(
            nullable = false
    )
    private String telefone;
    @Column(
            nullable = false,
            unique = true
    )
    private String email;
    @Column(
            nullable = false
    )
    private String endereco;
    @Column(
            nullable = false
    )
    private String convenio;
    @Column(
            nullable = false,
            unique = true
    )
    private String numeroCarteirinha;



}
