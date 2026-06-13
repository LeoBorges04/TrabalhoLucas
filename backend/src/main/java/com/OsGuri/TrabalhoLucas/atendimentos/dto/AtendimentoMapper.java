package com.OsGuri.TrabalhoLucas.atendimentos.dto;

import com.OsGuri.TrabalhoLucas.atendimentos.AtendimentoEntity;
import com.OsGuri.TrabalhoLucas.paciente.PacienteEntity;
import com.OsGuri.TrabalhoLucas.profissional.ProfissionalEntity;
import org.springframework.stereotype.Component;

@Component
public class AtendimentoMapper {

    public AtendimentoEntity map(AtendimentoRequestDto dto, PacienteEntity paciente, ProfissionalEntity profissional) {
        AtendimentoEntity entidade = new AtendimentoEntity();
        entidade.setPaciente(paciente);
        entidade.setProfissional(profissional);
        entidade.setEspecialidade(dto.getEspecialidade());
        entidade.setDataHora(dto.getDataHora());
        entidade.setTipo(dto.getTipo());
        entidade.setStatus(dto.getStatus());
        entidade.setDiagnostico(dto.getDiagnostico());
        entidade.setObservacoes(dto.getObservacoes());
        entidade.setValor(dto.getValor());
        return entidade;
    }

    public AtendimentoResponseDto map(AtendimentoEntity entidade) {
        AtendimentoResponseDto dto = new AtendimentoResponseDto();
        dto.setId(entidade.getId());
        if (entidade.getPaciente() != null) {
            dto.setPacienteId(entidade.getPaciente().getId());
            dto.setPacienteNome(entidade.getPaciente().getNome());
        }
        if (entidade.getProfissional() != null) {
            dto.setProfissionalId(entidade.getProfissional().getId());
            dto.setProfissionalNome(entidade.getProfissional().getNome());
            dto.setProfissionalCrm(entidade.getProfissional().getRegistroConselho());
        }
        dto.setEspecialidade(entidade.getEspecialidade());
        dto.setDataHora(entidade.getDataHora());
        dto.setTipo(entidade.getTipo());
        dto.setStatus(entidade.getStatus());
        dto.setDiagnostico(entidade.getDiagnostico());
        dto.setObservacoes(entidade.getObservacoes());
        dto.setValor(entidade.getValor());
        return dto;
    }
}
