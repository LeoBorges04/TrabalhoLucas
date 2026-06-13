package com.OsGuri.TrabalhoLucas.atendimentos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AtendimentoRepository extends JpaRepository<AtendimentoEntity, Long> {
    List<AtendimentoEntity> findAllByAtivoTrue();
    List<AtendimentoEntity> findByDataHoraBetweenAndAtivoTrue(LocalDateTime start, LocalDateTime end);
    List<AtendimentoEntity> findByPacienteIdAndVisivelPacienteTrueAndAtivoTrue(Long pacienteId);
    List<AtendimentoEntity> findByProfissionalIdAndVisivelProfissionalTrueAndAtivoTrue(Long profissionalId);
}
