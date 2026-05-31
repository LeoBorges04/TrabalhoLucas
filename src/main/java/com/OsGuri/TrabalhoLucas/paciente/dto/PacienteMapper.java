package com.OsGuri.TrabalhoLucas.paciente.dto;

import com.OsGuri.TrabalhoLucas.paciente.PacienteEntity;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {

    public PacienteEntity map(PacienteRequestDto pacRequest){
        PacienteEntity paciente = new PacienteEntity();
        paciente.setNomeCompleto(pacRequest.getNomeCompleto());
        paciente.setCpf(pacRequest.getCpf());
        paciente.setDataNascimento(pacRequest.getDataNascimento());
        paciente.setSexo(pacRequest.getSexo());
        paciente.setTelefone(pacRequest.getTelefone());
        paciente.setEmail(pacRequest.getEmail());
        paciente.setEndereco(pacRequest.getEndereco());
        paciente.setConvenio(pacRequest.getConvenio());
        paciente.setNumeroCarteirinha(pacRequest.getNumeroCarteirinha());
        paciente.setAtivo(true);

        return paciente;
    }

    public PacienteResponseDto map(PacienteEntity paciente){
        PacienteResponseDto dto = new PacienteResponseDto();
        dto.setId(paciente.getId());
        dto.setAtivo(paciente.isAtivo());
        dto.setNomeCompleto(paciente.getNomeCompleto());
        dto.setCpf(paciente.getCpf());
        dto.setDataNascimento(paciente.getDataNascimento());
        dto.setSexo(paciente.getSexo());
        dto.setTelefone(paciente.getTelefone());
        dto.setEmail(paciente.getEmail());
        dto.setConvenio(paciente.getConvenio());
        dto.setNumero_carteirinha(paciente.getNumeroCarteirinha());
        dto.setEndereco(paciente.getEndereco());
        return dto;
    }
}
