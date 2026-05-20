package com.example.demo.rest;

import com.example.demo.dto.AlertaRequestDTO;
import com.example.demo.dto.AlertaResponseDTO;
import com.example.demo.mapper.AlertaMapper;
import com.example.demo.model.Alerta;
import com.example.demo.service.AlertaService;
import com.example.demo.service.ParcelaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
public class AlertaRestController {

    private final AlertaService alertaService;
    private final ParcelaService parcelaService;
    private final AlertaMapper alertaMapper;

    @GetMapping
    public ResponseEntity<List<AlertaResponseDTO>> listar(
            @RequestParam(required = false) Alerta.EstadoAlerta estado,
            @RequestParam(required = false, defaultValue = "false") boolean deleted) {

        List<Alerta> alertas;

        if (deleted) {
            alertas = alertaService.findDeleted();
        } else if (estado != null) {
            alertas = alertaService.findAll().stream()
                    .filter(a -> a.getEstado() == estado)
                    .toList();
        } else {
            alertas = alertaService.findAll();
        }

        List<AlertaResponseDTO> response = alertas.stream()
                .map(alertaMapper::toDTO)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertaResponseDTO> obtener(@PathVariable Long id) {
        return alertaService.findById(id)
                .map(alertaMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/parcela/{parcelaId}")
    public ResponseEntity<List<AlertaResponseDTO>> porParcela(@PathVariable Long parcelaId) {
        List<AlertaResponseDTO> response = alertaService.findByParcelaId(parcelaId).stream()
                .map(alertaMapper::toDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody AlertaRequestDTO dto) {
        return parcelaService.findById(dto.getParcelaId()).map(parcela -> {
            Alerta alerta = alertaMapper.toEntity(dto);
            alerta.setParcela(parcela);
            Alerta guardada = alertaService.save(alerta);
            return ResponseEntity.status(HttpStatus.CREATED).body(alertaMapper.toDTO(guardada));
        }).orElse(ResponseEntity.badRequest().build());
    }

    @PatchMapping("/{id}/atender")
    public ResponseEntity<AlertaResponseDTO> atender(@PathVariable Long id) {
        try {
            Alerta atendida = alertaService.atender(id);
            return ResponseEntity.ok(alertaMapper.toDTO(atendida));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/descartar")
    public ResponseEntity<AlertaResponseDTO> descartar(@PathVariable Long id) {
        try {
            Alerta descartada = alertaService.descartar(id);
            return ResponseEntity.ok(alertaMapper.toDTO(descartada));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (alertaService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        alertaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/restaurar")
    public ResponseEntity<Void> restaurar(@PathVariable Long id) {
        alertaService.restoreById(id);
        return ResponseEntity.ok().build();
    }
}
