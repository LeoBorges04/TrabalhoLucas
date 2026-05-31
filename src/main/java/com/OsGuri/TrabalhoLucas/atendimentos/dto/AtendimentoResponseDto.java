package com.OsGuri.TrabalhoLucas.atendimentos.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AtendimentoResponseDto {
    private Long id;
    private Long pacienteId;
    private String pacienteNome;
    private Long profissionalId;
    private String profissionalNome;
    private LocalDateTime dataHora;
    private String tipo;
    private String status;
    private String diagnostico;
    private String observacoes;
    private Double valor;
}
