package com.OsGuri.TrabalhoLucas.especialidade;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_suespecialidade")
public class EspecialidadeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private boolean ativo = true;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private String area;
}
