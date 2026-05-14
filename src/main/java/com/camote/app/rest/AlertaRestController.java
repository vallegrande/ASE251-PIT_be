package com.camote.app.rest;

import com.camote.app.model.Alerta;
import com.camote.app.model.Alerta.NivelRiesgo;
import com.camote.app.service.AlertaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para gestionar operaciones de Alertas.
 * Proporciona endpoints para CRUD y operaciones adicionales con alertas de riesgo.
 */
@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class AlertaRestController {

    private final AlertaService alertaService;

    /**
     * Obtiene todas las alertas.
     *
     * @return lista de alertas
     */
    @GetMapping
    public ResponseEntity<List<Alerta>> obtenerTodas() {
        log.info("GET /api/alertas - Obteniendo todas las alertas");
        return ResponseEntity.ok(alertaService.obtenerTodas());
    }

    /**
     * Obtiene una alerta por ID.
     *
     * @param id ID de la alerta
     * @return alerta encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<Alerta> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/alertas/{} - Obteniendo alerta", id);
        return alertaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene alertas de una parcela.
     *
     * @param parcelaId ID de la parcela
     * @return lista de alertas
     */
    @GetMapping("/parcela/{parcelaId}")
    public ResponseEntity<List<Alerta>> obtenerPorParcela(@PathVariable Long parcelaId) {
        log.info("GET /api/alertas/parcela/{} - Obteniendo alertas de parcela", parcelaId);
        return ResponseEntity.ok(alertaService.obtenerPorParcela(parcelaId));
    }

    /**
     * Obtiene alertas pendientes de una parcela.
     *
     * @param parcelaId ID de la parcela
     * @return lista de alertas pendientes
     */
    @GetMapping("/parcela/{parcelaId}/pendientes")
    public ResponseEntity<List<Alerta>> obtenerPendientes(@PathVariable Long parcelaId) {
        log.info("GET /api/alertas/parcela/{}/pendientes - Obteniendo alertas pendientes", parcelaId);
        return ResponseEntity.ok(alertaService.obtenerPendientes(parcelaId));
    }

    /**
     * Obtiene alertas críticas de una parcela.
     *
     * @param parcelaId ID de la parcela
     * @return lista de alertas críticas
     */
    @GetMapping("/parcela/{parcelaId}/criticas")
    public ResponseEntity<List<Alerta>> obtenerCriticas(@PathVariable Long parcelaId) {
        log.info("GET /api/alertas/parcela/{}/criticas - Obteniendo alertas críticas", parcelaId);
        return ResponseEntity.ok(alertaService.obtenerCriticas(parcelaId));
    }

    /**
     * Obtiene alertas por nivel de riesgo.
     *
     * @param nivel nivel de riesgo
     * @return lista de alertas con ese nivel
     */
    @GetMapping("/nivel/{nivel}")
    public ResponseEntity<List<Alerta>> obtenerPorNivel(@PathVariable NivelRiesgo nivel) {
        log.info("GET /api/alertas/nivel/{} - Obteniendo alertas por nivel", nivel);
        return ResponseEntity.ok(alertaService.obtenerPorNivel(nivel));
    }

    /**
     * Obtiene alertas en un rango de fechas.
     *
     * @param fechaInicio fecha de inicio (ISO 8601)
     * @param fechaFin fecha de fin (ISO 8601)
     * @return lista de alertas en el rango
     */
    @GetMapping("/rango")
    public ResponseEntity<List<Alerta>> obtenerPorFecha(
            @RequestParam LocalDateTime fechaInicio,
            @RequestParam LocalDateTime fechaFin) {
        log.info("GET /api/alertas/rango - Obteniendo alertas entre {} y {}", fechaInicio, fechaFin);
        return ResponseEntity.ok(alertaService.obtenerPorFecha(fechaInicio, fechaFin));
    }

    /**
     * Crea una nueva alerta.
     *
     * @param alerta datos de la alerta
     * @return alerta creada
     */
    @PostMapping
    public ResponseEntity<Alerta> crear(@RequestBody Alerta alerta) {
        log.info("POST /api/alertas - Creando nueva alerta para parcela: {}", alerta.getParcela().getId());
        Alerta alertaCreada = alertaService.crear(alerta);
        return ResponseEntity.status(HttpStatus.CREATED).body(alertaCreada);
    }

    /**
     * Actualiza una alerta existente.
     *
     * @param id ID de la alerta
     * @param alerta datos actualizados
     * @return alerta actualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<Alerta> actualizar(@PathVariable Long id, @RequestBody Alerta alerta) {
        log.info("PUT /api/alertas/{} - Actualizando alerta", id);
        return alertaService.actualizar(id, alerta)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Marca una alerta como atendida.
     *
     * @param id ID de la alerta
     * @return alerta actualizada
     */
    @PatchMapping("/{id}/atender")
    public ResponseEntity<Alerta> marcarAtendida(@PathVariable Long id) {
        log.info("PATCH /api/alertas/{}/atender - Marcando alerta como atendida", id);
        return alertaService.marcarAtendida(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Descarta una alerta.
     *
     * @param id ID de la alerta
     * @return alerta actualizada
     */
    @PatchMapping("/{id}/descartar")
    public ResponseEntity<Alerta> descartar(@PathVariable Long id) {
        log.info("PATCH /api/alertas/{}/descartar - Descartando alerta", id);
        return alertaService.descartar(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina una alerta.
     *
     * @param id ID de la alerta
     * @return respuesta sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/alertas/{} - Eliminando alerta", id);
        alertaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene el conteo de alertas graves (críticas y altas sin atender) de una parcela.
     *
     * @param parcelaId ID de la parcela
     * @return cantidad de alertas graves
     */
    @GetMapping("/parcela/{parcelaId}/graves")
    public ResponseEntity<Long> contarGraves(@PathVariable Long parcelaId) {
        log.info("GET /api/alertas/parcela/{}/graves - Contando alertas graves", parcelaId);
        return ResponseEntity.ok(alertaService.contarAleratasGraves(parcelaId));
    }
}
