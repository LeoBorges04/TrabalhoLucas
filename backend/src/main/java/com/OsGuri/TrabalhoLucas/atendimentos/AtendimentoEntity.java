package com.OsGuri.TrabalhoLucas.atendimentos;

import com.OsGuri.TrabalhoLucas.paciente.PacienteEntity;
import com.OsGuri.TrabalhoLucas.especialidade.EspecialidadeEnum;
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

    private boolean ativo = true;

    @Column(name = "visivel_paciente")
    private Boolean visivelPaciente = true;

    @Column(name = "visivel_profissional")
    private Boolean visivelProfissional = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private PacienteEntity paciente;

    @ManyToOne(optional = true)
    @JoinColumn(name = "profissional_id", nullable = true)
    private ProfissionalEntity profissional;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EspecialidadeEnum especialidade;

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
