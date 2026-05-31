package com.OsGuri.TrabalhoLucas.especialidade;

import com.OsGuri.TrabalhoLucas.especialidade.dto.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/especialidades")
public class EspecialidadeController {

    private final EspecialidadeService especialidadeService;

    public EspecialidadeController(EspecialidadeService especialidadeService) {
        this.especialidadeService = especialidadeService;
    }

    @PostMapping
    public ResponseEntity<EspecialidadeResponseDto> cadastrar(@Valid @RequestBody EspecialidadeRequestDto dto) {
        EspecialidadeResponseDto response = especialidadeService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadeResponseDto> listar(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadeService.listar(id));
    }

    @GetMapping
    public ResponseEntity<List<EspecialidadeResponseDto>> listarTodos() {
        return ResponseEntity.ok(especialidadeService.listarRegistros());
    }

    @GetMapping("/ativas")
    public ResponseEntity<List<EspecialidadeResponseDto>> listarAtivos() {
        return ResponseEntity.ok(especialidadeService.listarAtivos());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EspecialidadeResponseDto> atualizar(@PathVariable Long id, @Valid @RequestBody EspecialidadePatchDto dto) {
        return ResponseEntity.ok(especialidadeService.atualizarParcial(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        especialidadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<EspecialidadeResponseDto> reativar(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadeService.reativar(id));
    }
}
