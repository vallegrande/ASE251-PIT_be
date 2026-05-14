package com.camote.app.rest;

import com.camote.app.service.AlertaService;
import com.camote.app.service.MonitoreoService;
import com.camote.app.service.ParcelaService;
import com.camote.app.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para el Dashboard del sistema.
 * Proporciona endpoints con información agregada y estadísticas del sistema.
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class DashboardRestController {

    private final ParcelaService parcelaService;
    private final MonitoreoService monitoreoService;
    private final AlertaService alertaService;
    private final UsuarioService usuarioService;

    /**
     * Obtiene un resumen general del sistema.
     * Incluye conteos de usuarios activos, parcelas, alertas críticas, etc.
     *
     * @return mapa con datos del dashboard
     */
    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> obtenerResumen() {
        log.info("GET /api/dashboard/resumen - Obteniendo resumen del sistema");
        
        Map<String, Object> resumen = new HashMap<>();
        
        try {
            resumen.put("usuariosActivos", usuarioService.contarActivos());
            resumen.put("totalParcelas", parcelaService.obtenerTodas().size());
            resumen.put("totalMonitoreos", monitoreoService.obtenerTodos().size());
            resumen.put("totalAlertas", alertaService.obtenerTodas().size());
            
            // Obtener alertas con nivel crítico
            long alertasCriticas = alertaService.obtenerTodas()
                    .stream()
                    .filter(a -> a.getNivelRiesgo().equals(com.camote.app.model.Alerta.NivelRiesgo.CRITICA))
                    .count();
            resumen.put("alertasCriticas", alertasCriticas);
            
            resumen.put("timestamp", System.currentTimeMillis());
            resumen.put("estado", "OK");
            
            return ResponseEntity.ok(resumen);
        } catch (Exception e) {
            log.error("Error al obtener resumen del dashboard: {}", e.getMessage(), e);
            resumen.put("estado", "ERROR");
            resumen.put("error", e.getMessage());
            return ResponseEntity.status(500).body(resumen);
        }
    }

    /**
     * Obtiene estadísticas de una parcela específica.
     *
     * @param parcelaId ID de la parcela
     * @return mapa con estadísticas de la parcela
     */
    @GetMapping("/parcela/{parcelaId}/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasParcela(@PathVariable Long parcelaId) {
        log.info("GET /api/dashboard/parcela/{}/estadisticas - Obteniendo estadísticas de parcela", parcelaId);
        
        Map<String, Object> estadisticas = new HashMap<>();
        
        try {
            estadisticas.put("parcelaId", parcelaId);
            estadisticas.put("totalMonitoreos", monitoreoService.contarMonitoreosParcela(parcelaId));
            
            // Monitoreo más reciente
            monitoreoService.obtenerUltimoMonitoreo(parcelaId).ifPresent(m -> 
                estadisticas.put("ultimoMonitoreo", m)
            );
            
            // Alertas
            long alertasPendientes = alertaService.obtenerPendientes(parcelaId).size();
            long alertasCriticas = alertaService.obtenerCriticas(parcelaId).size();
            
            estadisticas.put("alertasPendientes", alertasPendientes);
            estadisticas.put("alertasCriticas", alertasCriticas);
            estadisticas.put("alertasGraves", alertaService.contarAleratasGraves(parcelaId));
            
            estadisticas.put("timestamp", System.currentTimeMillis());
            estadisticas.put("estado", "OK");
            
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            log.error("Error al obtener estadísticas de parcela {}: {}", parcelaId, e.getMessage(), e);
            estadisticas.put("estado", "ERROR");
            estadisticas.put("error", e.getMessage());
            return ResponseEntity.status(500).body(estadisticas);
        }
    }

    /**
     * Obtiene estadísticas de un usuario específico.
     *
     * @param usuarioId ID del usuario
     * @return mapa con estadísticas del usuario
     */
    @GetMapping("/usuario/{usuarioId}/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasUsuario(@PathVariable Long usuarioId) {
        log.info("GET /api/dashboard/usuario/{}/estadisticas - Obteniendo estadísticas de usuario", usuarioId);
        
        Map<String, Object> estadisticas = new HashMap<>();
        
        try {
            estadisticas.put("usuarioId", usuarioId);
            
            // Parcelas del usuario
            var parcelas = parcelaService.obtenerPorUsuario(usuarioId);
            estadisticas.put("totalParcelas", parcelas.size());
            estadisticas.put("parcelasActivas", parcelaService.contarActivasPorUsuario(usuarioId));
            
            // Alertas totales del usuario
            long alertasTotales = parcelas.stream()
                    .flatMap(p -> alertaService.obtenerPorParcela(p.getId()).stream())
                    .count();
            
            long alertasPendientes = parcelas.stream()
                    .flatMap(p -> alertaService.obtenerPendientes(p.getId()).stream())
                    .count();
            
            estadisticas.put("alertasTotales", alertasTotales);
            estadisticas.put("alertasPendientes", alertasPendientes);
            
            estadisticas.put("timestamp", System.currentTimeMillis());
            estadisticas.put("estado", "OK");
            
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            log.error("Error al obtener estadísticas de usuario {}: {}", usuarioId, e.getMessage(), e);
            estadisticas.put("estado", "ERROR");
            estadisticas.put("error", e.getMessage());
            return ResponseEntity.status(500).body(estadisticas);
        }
    }

    /**
     * Obtiene estado de salud del sistema.
     *
     * @return estado del sistema
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> verificarSalud() {
        log.info("GET /api/dashboard/health - Verificando salud del sistema");
        
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", System.currentTimeMillis());
        health.put("servicio", "Sistema de Gestión de Riesgos Agrícolas");
        
        return ResponseEntity.ok(health);
    }
}
