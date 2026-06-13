package com.OsGuri.TrabalhoLucas.profissional;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.OsGuri.TrabalhoLucas.especialidade.EspecialidadeEnum;
import java.util.Optional;

public interface ProfissionalRepository extends JpaRepository<ProfissionalEntity, Long> {
    Optional<ProfissionalEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByRegistroConselho(String registroConselho);
    List<ProfissionalEntity> findAllByAtivoTrue();
    List<ProfissionalEntity> findByEspecialidadesContainingAndAtivoTrue(EspecialidadeEnum especialidade);
}
