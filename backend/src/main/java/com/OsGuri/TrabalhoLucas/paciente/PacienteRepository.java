package com.OsGuri.TrabalhoLucas.paciente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository <PacienteEntity, Long> {
    Optional<PacienteEntity> findByEmail(String email);
    List<PacienteEntity> findAllByAtivoTrue();

    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
}
