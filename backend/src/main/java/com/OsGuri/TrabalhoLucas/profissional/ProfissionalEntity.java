package com.OsGuri.TrabalhoLucas.profissional;

import com.OsGuri.TrabalhoLucas.especialidade.EspecialidadeEnum;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "tb_profissional_especialidades", joinColumns = @JoinColumn(name = "profissional_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "especialidade")
    private List<EspecialidadeEnum> especialidades;

    @Column(nullable = false)
    private String cargo;

    @Column(nullable = false)
    private String turno;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false, unique = true)
    private String email;
}
