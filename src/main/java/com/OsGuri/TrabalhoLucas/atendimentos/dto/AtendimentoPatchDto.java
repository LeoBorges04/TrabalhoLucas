package com.OsGuri.TrabalhoLucas.atendimentos.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AtendimentoPatchDto {
    private LocalDateTime dataHora;
    private String tipo;
    private String status;
    private String diagnostico;
    private String observacoes;
    private Double valor;
}
