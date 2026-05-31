package com.OsGuri.TrabalhoLucas.paciente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PacienteRepository extends JpaRepository <PacienteEntity, Long> {
    List<PacienteEntity> findAllByAtivoTrue();

    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
}
