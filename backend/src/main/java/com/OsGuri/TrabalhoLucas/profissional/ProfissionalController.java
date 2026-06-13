package com.OsGuri.TrabalhoLucas.profissional;

import com.OsGuri.TrabalhoLucas.profissional.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.OsGuri.TrabalhoLucas.especialidade.EspecialidadeEnum;

import java.util.List;

@RestController
@RequestMapping("/api/profissionais")
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    public ProfissionalController(ProfissionalService profissionalService) {
        this.profissionalService = profissionalService;
    }

    @PostMapping
    public ResponseEntity<ProfissionalResponseDto> cadastrar(@Valid @RequestBody ProfissionalRequestDto dto) {
        ProfissionalResponseDto response = profissionalService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<ProfissionalResponseDto> obterMeuPerfil() {
        return ResponseEntity.ok(profissionalService.obterMeuPerfil());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalResponseDto> listar(@PathVariable Long id) {
        return ResponseEntity.ok(profissionalService.listar(id));
    }

    @GetMapping
    public ResponseEntity<List<ProfissionalResponseDto>> listarTodos() {
        return ResponseEntity.ok(profissionalService.listarRegistros());
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<ProfissionalResponseDto>> listarAtivos() {
        return ResponseEntity.ok(profissionalService.listarAtivos());
    }

    @GetMapping("/especialidade/{especialidade}")
    public ResponseEntity<List<ProfissionalResponseDto>> listarPorEspecialidade(@PathVariable EspecialidadeEnum especialidade) {
        return ResponseEntity.ok(profissionalService.listarPorEspecialidade(especialidade));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProfissionalResponseDto> atualizar(@PathVariable Long id, @Valid @RequestBody ProfissionalPatchDto dto) {
        return ResponseEntity.ok(profissionalService.atualizarParcial(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        profissionalService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<ProfissionalResponseDto> reativar(@PathVariable Long id) {
        return ResponseEntity.ok(profissionalService.reativar(id));
    }
}
