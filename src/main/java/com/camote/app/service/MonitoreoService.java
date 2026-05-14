package com.camote.app.service;

import com.camote.app.model.Monitoreo;
import com.camote.app.model.Monitoreo.EstadoMonitoreo;
import com.camote.app.repository.MonitoreoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar operaciones relacionadas con Monitoreos.
 * Implementa la lógica de negocio para registrar y consultar monitoreos.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MonitoreoService {

    private final MonitoreoRepository monitoreoRepository;

    /**
     * Obtiene todos los monitoreos del sistema.
     *
     * @return lista de todos los monitoreos
     */
    @Transactional(readOnly = true)
    public List<Monitoreo> obtenerTodos() {
        log.info("Obteniendo todos los monitoreos");
        return monitoreoRepository.findAll();
    }

    /**
     * Obtiene un monitoreo por su ID.
     *
     * @param id ID del monitoreo
     * @return Optional con el monitoreo si existe
     */
    @Transactional(readOnly = true)
    public Optional<Monitoreo> obtenerPorId(Long id) {
        log.info("Obteniendo monitoreo con ID: {}", id);
        return monitoreoRepository.findById(id);
    }

    /**
     * Obtiene todos los monitoreos de una parcela.
     *
     * @param parcelaId ID de la parcela
     * @return lista de monitoreos
     */
    @Transactional(readOnly = true)
    public List<Monitoreo> obtenerPorParcela(Long parcelaId) {
        log.info("Obteniendo monitoreos de la parcela: {}", parcelaId);
        return monitoreoRepository.findByParcelaId(parcelaId);
    }

    /**
     * Obtiene monitoreos de una parcela en un rango de fechas.
     *
     * @param parcelaId ID de la parcela
     * @param fechaInicio fecha de inicio
     * @param fechaFin fecha de fin
     * @return lista de monitoreos en el rango
     */
    @Transactional(readOnly = true)
    public List<Monitoreo> obtenerMonitoreosPorFecha(Long parcelaId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.info("Obteniendo monitoreos de parcela {} entre {} y {}", parcelaId, fechaInicio, fechaFin);
        return monitoreoRepository.findMonitoreosPorFecha(parcelaId, fechaInicio, fechaFin);
    }

    /**
     * Obtiene el monitoreo más reciente de una parcela.
     *
     * @param parcelaId ID de la parcela
     * @return Optional con el monitoreo más reciente
     */
    @Transactional(readOnly = true)
    public Optional<Monitoreo> obtenerUltimoMonitoreo(Long parcelaId) {
        log.info("Obteniendo último monitoreo de parcela: {}", parcelaId);
        return Optional.ofNullable(monitoreoRepository.findUltimoMonitoreo(parcelaId));
    }

    /**
     * Obtiene monitoreos completados de una parcela.
     *
     * @param parcelaId ID de la parcela
     * @return lista de monitoreos completados
     */
    @Transactional(readOnly = true)
    public List<Monitoreo> obtenerCompletados(Long parcelaId) {
        log.info("Obteniendo monitoreos completados de parcela: {}", parcelaId);
        return monitoreoRepository.findByParcelaIdAndEstado(parcelaId, EstadoMonitoreo.COMPLETADO);
    }

    /**
     * Crea un nuevo monitoreo.
     *
     * @param monitoreo datos del monitoreo a crear
     * @return monitoreo creado
     */
    public Monitoreo crear(Monitoreo monitoreo) {
        log.info("Creando nuevo monitoreo para parcela: {}", monitoreo.getParcela().getId());
        monitoreo.setFechaMonitoreo(LocalDateTime.now());
        monitoreo.setEstado(EstadoMonitoreo.COMPLETADO);
        return monitoreoRepository.save(monitoreo);
    }

    /**
     * Actualiza un monitoreo existente.
     *
     * @param id ID del monitoreo a actualizar
     * @param monitoreoActualizado datos actualizados
     * @return monitoreo actualizado
     */
    public Optional<Monitoreo> actualizar(Long id, Monitoreo monitoreoActualizado) {
        log.info("Actualizando monitoreo con ID: {}", id);
        return monitoreoRepository.findById(id).map(monitoreo -> {
            monitoreo.setTemperatura(monitoreoActualizado.getTemperatura());
            monitoreo.setHumedad(monitoreoActualizado.getHumedad());
            monitoreo.setPrecipitacion(monitoreoActualizado.getPrecipitacion());
            monitoreo.setVelocidadViento(monitoreoActualizado.getVelocidadViento());
            monitoreo.setObservaciones(monitoreoActualizado.getObservaciones());
            monitoreo.setEstado(monitoreoActualizado.getEstado());
            return monitoreoRepository.save(monitoreo);
        });
    }

    /**
     * Elimina un monitoreo.
     *
     * @param id ID del monitoreo a eliminar
     */
    public void eliminar(Long id) {
        log.info("Eliminando monitoreo con ID: {}", id);
        monitoreoRepository.deleteById(id);
    }

    /**
     * Cuenta los monitoreos de una parcela.
     *
     * @param parcelaId ID de la parcela
     * @return cantidad de monitoreos
     */
    @Transactional(readOnly = true)
    public Long contarMonitoreosParcela(Long parcelaId) {
        log.info("Contando monitoreos de parcela: {}", parcelaId);
        return monitoreoRepository.countByParcelaId(parcelaId);
    }
}
