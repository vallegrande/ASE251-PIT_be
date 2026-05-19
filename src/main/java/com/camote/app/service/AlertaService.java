package com.camote.app.service;

import com.camote.app.model.Alerta;
import com.camote.app.model.Alerta.EstadoAlerta;
import com.camote.app.model.Alerta.NivelRiesgo;
import com.camote.app.repository.AlertaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Servicio para gestionar operaciones relacionadas con Alertas.
 * Implementa la lógica de negocio para crear, actualizar y consultar alertas de riesgo.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AlertaService {

    private final AlertaRepository alertaRepository;

    /**
     * Obtiene todas las alertas del sistema.
     *
     * @return lista de todas las alertas
     */
    @Transactional(readOnly = true)
    public List<Alerta> obtenerTodas() {
        log.info("Obteniendo todas las alertas");
        return alertaRepository.findAll();
    }

    /**
     * Obtiene una alerta por su ID.
     *
     * @param id ID de la alerta
     * @return Optional con la alerta si existe
     */
    @Transactional(readOnly = true)
    public Optional<Alerta> obtenerPorId(@NonNull Long id) {
        Objects.requireNonNull(id);
        log.info("Obteniendo alerta con ID: {}", id);
        return alertaRepository.findById(id);
    }

    /**
     * Obtiene todas las alertas de una parcela.
     *
     * @param parcelaId ID de la parcela
     * @return lista de alertas
     */
    @Transactional(readOnly = true)
    public List<Alerta> obtenerPorParcela(Long parcelaId) {
        log.info("Obteniendo alertas de la parcela: {}", parcelaId);
        return alertaRepository.findByParcelaId(parcelaId);
    }

    /**
     * Obtiene alertas pendientes de una parcela.
     *
     * @param parcelaId ID de la parcela
     * @return lista de alertas pendientes
     */
    @Transactional(readOnly = true)
    public List<Alerta> obtenerPendientes(Long parcelaId) {
        log.info("Obteniendo alertas pendientes de parcela: {}", parcelaId);
        return alertaRepository.findByParcelaIdAndEstado(parcelaId, EstadoAlerta.PENDIENTE);
    }

    /**
     * Obtiene alertas críticas de una parcela.
     *
     * @param parcelaId ID de la parcela
     * @return lista de alertas críticas
     */
    @Transactional(readOnly = true)
    public List<Alerta> obtenerCriticas(Long parcelaId) {
        log.info("Obteniendo alertas críticas de parcela: {}", parcelaId);
        return alertaRepository.findAlertasCriticas(parcelaId, NivelRiesgo.CRITICA);
    }

    /**
     * Obtiene alertas por nivel de riesgo.
     *
     * @param nivel nivel de riesgo
     * @return lista de alertas con ese nivel
     */
    @Transactional(readOnly = true)
    public List<Alerta> obtenerPorNivel(NivelRiesgo nivel) {
        log.info("Obteniendo alertas por nivel de riesgo: {}", nivel);
        return alertaRepository.findByNivelRiesgo(nivel);
    }

    /**
     * Obtiene alertas en un rango de fechas.
     *
     * @param fechaInicio fecha de inicio
     * @param fechaFin fecha de fin
     * @return lista de alertas
     */
    @Transactional(readOnly = true)
    public List<Alerta> obtenerPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.info("Obteniendo alertas entre {} y {}", fechaInicio, fechaFin);
        return alertaRepository.findByFechaBetween(fechaInicio, fechaFin);
    }

    /**
     * Crea una nueva alerta.
     *
     * @param alerta datos de la alerta a crear
     * @return alerta creada
     */
    public Alerta crear(Alerta alerta) {
        log.info("Creando nueva alerta para parcela: {}", alerta.getParcela().getId());
        alerta.setFecha(LocalDateTime.now());
        alerta.setEstado(EstadoAlerta.PENDIENTE);
        return alertaRepository.save(alerta);
    }

    /**
     * Actualiza una alerta existente.
     *
     * @param id ID de la alerta a actualizar
     * @param alertaActualizada datos actualizados
     * @return alerta actualizada
     */
    public Optional<Alerta> actualizar(@NonNull Long id, Alerta alertaActualizada) {
        Objects.requireNonNull(id);
        log.info("Actualizando alerta con ID: {}", id);
        return alertaRepository.findById(id).map(alerta -> {
            alerta.setTipo(alertaActualizada.getTipo());
            alerta.setMensaje(alertaActualizada.getMensaje());
            alerta.setNivelRiesgo(alertaActualizada.getNivelRiesgo());
            alerta.setEstado(alertaActualizada.getEstado());
            return alertaRepository.save(alerta);
        });
    }

    /**
     * Marca una alerta como atendida.
     *
     * @param id ID de la alerta
     * @return alerta actualizada
     */
    public Optional<Alerta> marcarAtendida(@NonNull Long id) {
        Objects.requireNonNull(id);
        log.info("Marcando alerta {} como atendida", id);
        return alertaRepository.findById(id).map(alerta -> {
            alerta.setEstado(EstadoAlerta.ATENDIDA);
            return alertaRepository.save(alerta);
        });
    }

    /**
     * Descarta una alerta.
     *
     * @param id ID de la alerta
     * @return alerta actualizada
     */
    public Optional<Alerta> descartar(@NonNull Long id) {
        Objects.requireNonNull(id);
        log.info("Descartando alerta {}", id);
        return alertaRepository.findById(id).map(alerta -> {
            alerta.setEstado(EstadoAlerta.DESCARTADA);
            return alertaRepository.save(alerta);
        });
    }

    /**
     * Elimina una alerta.
     *
     * @param id ID de la alerta a eliminar
     */
    public void eliminar(@NonNull Long id) {
        Objects.requireNonNull(id);
        log.info("Eliminando alerta con ID: {}", id);
        alertaRepository.deleteById(id);
    }

    /**
     * Cuenta alertas graves (críticas y altas sin atender) de una parcela.
     *
     * @param parcelaId ID de la parcela
     * @return cantidad de alertas graves
     */
    @Transactional(readOnly = true)
    public Long contarAleratasGraves(Long parcelaId) {
        log.info("Contando alertas graves de parcela: {}", parcelaId);
        return alertaRepository.countAlertasGraves(parcelaId);
    }
}
