package com.OsGuri.TrabalhoLucas.profissional;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfissionalRepository extends JpaRepository<ProfissionalEntity, Long> {
    boolean existsByEmail(String email);
    boolean existsByRegistroConselho(String registroConselho);
    List<ProfissionalEntity> findAllByAtivoTrue();
}
