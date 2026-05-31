package com.OsGuri.TrabalhoLucas.paciente;

import com.OsGuri.TrabalhoLucas.paciente.dto.PacientePatchDto;
import com.OsGuri.TrabalhoLucas.paciente.dto.PacienteRequestDto;
import com.OsGuri.TrabalhoLucas.paciente.dto.PacienteResponseDto;
import org.springframework.web.bind.annotation.RestController;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
public class PacienteController {
    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService){
        this.pacienteService = pacienteService;
    }
    @PostMapping("/pacientes")
    public ResponseEntity<PacienteResponseDto> criarPaciente(@Valid @RequestBody PacienteRequestDto dto){
        PacienteResponseDto paciente = pacienteService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(paciente);
    }

    @GetMapping("/pacientes/{id}")
    public ResponseEntity<PacienteResponseDto> listarPaciente(@PathVariable Long id){
        PacienteResponseDto paciente =  pacienteService.listar(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(paciente);
    }

    @GetMapping("/pacientes")
    public ResponseEntity<List<PacienteResponseDto>> listarTodos(){
        List<PacienteResponseDto> pacientes = pacienteService.listarRegistros();
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/pacientes/ativos")
    public ResponseEntity<List<PacienteResponseDto>> listarAtivos(){
        List<PacienteResponseDto> pacientes = pacienteService.listarAtivos();
        return ResponseEntity.ok(pacientes);
    }

    @PatchMapping("/pacientes/{id}")
    public ResponseEntity<PacienteResponseDto> atualizar(@PathVariable Long id, @Valid @RequestBody PacientePatchDto paciente){
        PacienteResponseDto dto = pacienteService.atualizarParcial(id,paciente);
        return ResponseEntity.ok(dto);
    }

    //Soft delete
    @DeleteMapping("/pacientes/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        pacienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/pacientes/{id}/reativar")
    public ResponseEntity<PacienteResponseDto> reativar(@PathVariable Long id){
        PacienteResponseDto paciente = pacienteService.reativar(id);
        return ResponseEntity.ok(paciente);
    }


}
