package com.OsGuri.TrabalhoLucas.atendimentos;

import com.OsGuri.TrabalhoLucas.paciente.PacienteEntity;
import com.OsGuri.TrabalhoLucas.profissional.ProfissionalEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_suatendimento")
public class AtendimentoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private PacienteEntity paciente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profissional_id", nullable = false)
    private ProfissionalEntity profissional;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String status;

    private String diagnostico;

    private String observacoes;

    private Double valor;
}
