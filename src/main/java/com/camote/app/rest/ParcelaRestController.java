package com.camote.app.rest;

import com.camote.app.model.Parcela;
import com.camote.app.model.Parcela.EstadoParcela;
import com.camote.app.service.ParcelaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar operaciones de Parcelas.
 * Proporciona endpoints para CRUD y operaciones adicionales con parcelas.
 */
@RestController
@RequestMapping("/api/parcelas")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ParcelaRestController {

    private final ParcelaService parcelaService;

    /**
     * Obtiene todas las parcelas.
     *
     * @return lista de parcelas
     */
    @GetMapping
    public ResponseEntity<List<Parcela>> obtenerTodas() {
        log.info("GET /api/parcelas - Obteniendo todas las parcelas");
        return ResponseEntity.ok(parcelaService.obtenerTodas());
    }

    /**
     * Obtiene una parcela por ID.
     *
     * @param id ID de la parcela
     * @return parcela encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<Parcela> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/parcelas/{} - Obteniendo parcela", id);
        return parcelaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene parcelas de un usuario.
     *
     * @param usuarioId ID del usuario
     * @return lista de parcelas del usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Parcela>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        log.info("GET /api/parcelas/usuario/{} - Obteniendo parcelas del usuario", usuarioId);
        return ResponseEntity.ok(parcelaService.obtenerPorUsuario(usuarioId));
    }

    /**
     * Busca parcelas por nombre.
     *
     * @param nombre nombre a buscar
     * @return lista de parcelas encontradas
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Parcela>> buscarPorNombre(@RequestParam String nombre) {
        log.info("GET /api/parcelas/buscar?nombre={} - Buscando parcelas", nombre);
        return ResponseEntity.ok(parcelaService.buscarPorNombre(nombre));
    }

    /**
     * Obtiene parcelas por estado.
     *
     * @param estado estado de la parcela
     * @return lista de parcelas con ese estado
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Parcela>> obtenerPorEstado(@PathVariable EstadoParcela estado) {
        log.info("GET /api/parcelas/estado/{} - Obteniendo parcelas por estado", estado);
        return ResponseEntity.ok(parcelaService.obtenerPorEstado(estado));
    }

    /**
     * Crea una nueva parcela.
     *
     * @param parcela datos de la parcela
     * @return parcela creada
     */
    @PostMapping
    public ResponseEntity<Parcela> crear(@RequestBody Parcela parcela) {
        log.info("POST /api/parcelas - Creando nueva parcela: {}", parcela.getNombre());
        Parcela parcelaCreada = parcelaService.crear(parcela);
        return ResponseEntity.status(HttpStatus.CREATED).body(parcelaCreada);
    }

    /**
     * Actualiza una parcela existente.
     *
     * @param id ID de la parcela
     * @param parcela datos actualizados
     * @return parcela actualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<Parcela> actualizar(@PathVariable Long id, @RequestBody Parcela parcela) {
        log.info("PUT /api/parcelas/{} - Actualizando parcela", id);
        return parcelaService.actualizar(id, parcela)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cambia el estado de una parcela.
     *
     * @param id ID de la parcela
     * @param estado nuevo estado
     * @return parcela con estado actualizado
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Parcela> cambiarEstado(@PathVariable Long id, @RequestParam EstadoParcela estado) {
        log.info("PATCH /api/parcelas/{}/estado - Cambiando estado a {}", id, estado);
        return parcelaService.cambiarEstado(id, estado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina una parcela.
     *
     * @param id ID de la parcela
     * @return respuesta sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/parcelas/{} - Eliminando parcela", id);
        parcelaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene el conteo de parcelas activas de un usuario.
     *
     * @param usuarioId ID del usuario
     * @return cantidad de parcelas activas
     */
    @GetMapping("/usuario/{usuarioId}/activas")
    public ResponseEntity<Long> contarActivasPorUsuario(@PathVariable Long usuarioId) {
        log.info("GET /api/parcelas/usuario/{}/activas - Contando parcelas activas", usuarioId);
        return ResponseEntity.ok(parcelaService.contarActivasPorUsuario(usuarioId));
    }
}
