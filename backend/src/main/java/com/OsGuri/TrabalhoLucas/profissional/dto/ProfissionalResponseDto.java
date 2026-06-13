package com.OsGuri.TrabalhoLucas.profissional.dto;

import com.OsGuri.TrabalhoLucas.especialidade.EspecialidadeEnum;
import java.util.List;
import lombok.Data;

@Data
public class ProfissionalResponseDto {
    private Long id;
    private boolean ativo;
    private String nome;
    private String registroConselho;
    private List<EspecialidadeEnum> especialidades;
    private String cargo;
    private String turno;
    private String telefone;
    private String email;
}
