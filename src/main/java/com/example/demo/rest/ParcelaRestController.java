package com.example.demo.rest;

import com.example.demo.dto.ParcelaRequestDTO;
import com.example.demo.dto.ParcelaResponseDTO;
import com.example.demo.mapper.ParcelaMapper;
import com.example.demo.model.Parcela;
import com.example.demo.model.Parcela.EstadoParcela;
import com.example.demo.service.ParcelaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parcelas")
@RequiredArgsConstructor
public class ParcelaRestController {

    private final ParcelaService parcelaService;
    private final ParcelaMapper parcelaMapper;

    @GetMapping
    public ResponseEntity<List<ParcelaResponseDTO>> listar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) EstadoParcela estado,
            @RequestParam(required = false, defaultValue = "false") boolean deleted) {

        List<Parcela> parcelas;

        if (deleted) {
            parcelas = parcelaService.findDeleted();
        } else if (nombre != null && !nombre.isBlank()) {
            parcelas = parcelaService.findByNombre(nombre);
        } else if (estado != null) {
            parcelas = parcelaService.findByEstado(estado);
        } else {
            parcelas = parcelaService.findAll();
        }

        List<ParcelaResponseDTO> response = parcelas.stream()
                .map(parcelaMapper::toDTO)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParcelaResponseDTO> obtener(@PathVariable Long id) {
        return parcelaService.findById(id)
                .map(parcelaMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ParcelaResponseDTO> crear(@Valid @RequestBody ParcelaRequestDTO dto) {
        Parcela parcela = parcelaMapper.toEntity(dto);
        Parcela guardada = parcelaService.save(parcela);
        return ResponseEntity.status(HttpStatus.CREATED).body(parcelaMapper.toDTO(guardada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParcelaResponseDTO> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody ParcelaRequestDTO dto) {
        return parcelaService.findById(id).map(parcela -> {
            parcelaMapper.updateEntity(parcela, dto);
            Parcela actualizada = parcelaService.save(parcela);
            return ResponseEntity.ok(parcelaMapper.toDTO(actualizada));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (parcelaService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        parcelaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/restaurar")
    public ResponseEntity<Void> restaurar(@PathVariable Long id) {
        parcelaService.restoreById(id);
        return ResponseEntity.ok().build();
    }
}
