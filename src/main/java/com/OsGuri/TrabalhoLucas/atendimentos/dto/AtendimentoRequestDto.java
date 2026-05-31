package com.OsGuri.TrabalhoLucas.atendimentos.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AtendimentoRequestDto {
    private Long pacienteId;
    private Long profissionalId;
    private LocalDateTime dataHora;
    private String tipo;
    private String status;
    private String diagnostico;
    private String observacoes;
    private Double valor;
}
