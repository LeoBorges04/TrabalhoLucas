package com.OsGuri.TrabalhoLucas.atendimentos;

import com.OsGuri.TrabalhoLucas.atendimentos.dto.AtendimentoPatchDto;
import com.OsGuri.TrabalhoLucas.atendimentos.dto.AtendimentoRequestDto;
import com.OsGuri.TrabalhoLucas.atendimentos.dto.AtendimentoResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/atendimentos")
public class AtendimentoController {

    private final AtendimentoService atendimentoService;

    public AtendimentoController(AtendimentoService atendimentoService) {
        this.atendimentoService = atendimentoService;
    }

    @PostMapping
    public ResponseEntity<AtendimentoResponseDto> create(@RequestBody AtendimentoRequestDto dto) {
        AtendimentoResponseDto response = atendimentoService.create(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AtendimentoResponseDto>> getAll(@RequestParam(required = false) LocalDate data) {
        if (data != null) {
            List<AtendimentoResponseDto> response = atendimentoService.findByData(data.atStartOfDay(), data.atTime(LocalTime.MAX));
            return ResponseEntity.ok(response);
        }
        List<AtendimentoResponseDto> response = atendimentoService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtendimentoResponseDto> getById(@PathVariable Long id) {
        AtendimentoResponseDto response = atendimentoService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AtendimentoResponseDto> update(@PathVariable Long id, @RequestBody AtendimentoPatchDto dto) {
        AtendimentoResponseDto response = atendimentoService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AtendimentoResponseDto> updatePut(@PathVariable Long id, @RequestBody AtendimentoPatchDto dto) {
        AtendimentoResponseDto response = atendimentoService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        atendimentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
