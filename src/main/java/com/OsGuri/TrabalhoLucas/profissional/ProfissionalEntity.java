package com.OsGuri.TrabalhoLucas.profissional;

import com.OsGuri.TrabalhoLucas.especialidade.EspecialidadeEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_suprofissional")
public class ProfissionalEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private boolean ativo = true;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String registroConselho;

    @ManyToOne(optional = false)
    @JoinColumn(name = "especialidade_id", nullable = false)
    private EspecialidadeEntity especialidade;

    @Column(nullable = false)
    private String cargo;

    @Column(nullable = false)
    private String turno;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false, unique = true)
    private String email;
}
