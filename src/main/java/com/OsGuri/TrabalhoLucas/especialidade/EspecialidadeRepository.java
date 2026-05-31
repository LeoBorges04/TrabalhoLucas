package com.OsGuri.TrabalhoLucas.especialidade;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EspecialidadeRepository extends JpaRepository<EspecialidadeEntity, Long> {
    boolean existsByNome(String nome);
    List<EspecialidadeEntity> findAllByAtivoTrue();
}
