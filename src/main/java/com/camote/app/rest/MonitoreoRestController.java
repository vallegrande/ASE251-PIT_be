package com.camote.app.rest;

import com.camote.app.model.Monitoreo;
import com.camote.app.service.MonitoreoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Controlador REST para gestionar operaciones de Monitoreos.
 * Proporciona endpoints para CRUD y consultas de registros de monitoreo.
 */
@RestController
@RequestMapping("/api/monitoreos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class MonitoreoRestController {

    private final MonitoreoService monitoreoService;

    /**
     * Obtiene todos los monitoreos.
     *
     * @return lista de monitoreos
     */
    @GetMapping
    public ResponseEntity<List<Monitoreo>> obtenerTodos() {
        log.info("GET /api/monitoreos - Obteniendo todos los monitoreos");
        return ResponseEntity.ok(monitoreoService.obtenerTodos());
    }

    /**
     * Obtiene un monitoreo por ID.
     *
     * @param id ID del monitoreo
     * @return monitoreo encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<Monitoreo> obtenerPorId(@PathVariable @NonNull Long id) {
        Objects.requireNonNull(id);
        log.info("GET /api/monitoreos/{} - Obteniendo monitoreo", id);
        return monitoreoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene monitoreos de una parcela.
     *
     * @param parcelaId ID de la parcela
     * @return lista de monitoreos
     */
    @GetMapping("/parcela/{parcelaId}")
    public ResponseEntity<List<Monitoreo>> obtenerPorParcela(@PathVariable Long parcelaId) {
        log.info("GET /api/monitoreos/parcela/{} - Obteniendo monitoreos de parcela", parcelaId);
        return ResponseEntity.ok(monitoreoService.obtenerPorParcela(parcelaId));
    }

    /**
     * Obtiene el último monitoreo de una parcela.
     *
     * @param parcelaId ID de la parcela
     * @return último monitoreo
     */
    @GetMapping("/parcela/{parcelaId}/ultimo")
    public ResponseEntity<Monitoreo> obtenerUltimo(@PathVariable Long parcelaId) {
        log.info("GET /api/monitoreos/parcela/{}/ultimo - Obteniendo último monitoreo", parcelaId);
        return monitoreoService.obtenerUltimoMonitoreo(parcelaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene monitoreos completados de una parcela.
     *
     * @param parcelaId ID de la parcela
     * @return lista de monitoreos completados
     */
    @GetMapping("/parcela/{parcelaId}/completados")
    public ResponseEntity<List<Monitoreo>> obtenerCompletados(@PathVariable Long parcelaId) {
        log.info("GET /api/monitoreos/parcela/{}/completados - Obteniendo monitoreos completados", parcelaId);
        return ResponseEntity.ok(monitoreoService.obtenerCompletados(parcelaId));
    }

    /**
     * Obtiene monitoreos de una parcela en un rango de fechas.
     *
     * @param parcelaId ID de la parcela
     * @param fechaInicio fecha de inicio (ISO 8601)
     * @param fechaFin fecha de fin (ISO 8601)
     * @return lista de monitoreos en el rango
     */
    @GetMapping("/parcela/{parcelaId}/rango")
    public ResponseEntity<List<Monitoreo>> obtenerPorRango(
            @PathVariable Long parcelaId,
            @RequestParam LocalDateTime fechaInicio,
            @RequestParam LocalDateTime fechaFin) {
        log.info("GET /api/monitoreos/parcela/{}/rango - Obteniendo monitoreos entre {} y {}", 
                parcelaId, fechaInicio, fechaFin);
        return ResponseEntity.ok(monitoreoService.obtenerMonitoreosPorFecha(parcelaId, fechaInicio, fechaFin));
    }

    /**
     * Crea un nuevo monitoreo.
     *
     * @param monitoreo datos del monitoreo
     * @return monitoreo creado
     */
    @PostMapping
    public ResponseEntity<Monitoreo> crear(@RequestBody Monitoreo monitoreo) {
        log.info("POST /api/monitoreos - Creando nuevo monitoreo para parcela: {}", 
                monitoreo.getParcela().getId());
        Monitoreo monitoreoCreado = monitoreoService.crear(monitoreo);
        return ResponseEntity.status(HttpStatus.CREATED).body(monitoreoCreado);
    }

    /**
     * Actualiza un monitoreo existente.
     *
     * @param id ID del monitoreo
     * @param monitoreo datos actualizados
     * @return monitoreo actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<Monitoreo> actualizar(@PathVariable @NonNull Long id, @RequestBody Monitoreo monitoreo) {
        Objects.requireNonNull(id);
        log.info("PUT /api/monitoreos/{} - Actualizando monitoreo", id);
        return monitoreoService.actualizar(id, monitoreo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un monitoreo.
     *
     * @param id ID del monitoreo
     * @return respuesta sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable @NonNull Long id) {
        Objects.requireNonNull(id);
        log.info("DELETE /api/monitoreos/{} - Eliminando monitoreo", id);
        monitoreoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene el conteo de monitoreos de una parcela.
     *
     * @param parcelaId ID de la parcela
     * @return cantidad de monitoreos
     */
    @GetMapping("/parcela/{parcelaId}/contar")
    public ResponseEntity<Long> contarMonitoreos(@PathVariable Long parcelaId) {
        log.info("GET /api/monitoreos/parcela/{}/contar - Contando monitoreos", parcelaId);
        return ResponseEntity.ok(monitoreoService.contarMonitoreosParcela(parcelaId));
    }
}
