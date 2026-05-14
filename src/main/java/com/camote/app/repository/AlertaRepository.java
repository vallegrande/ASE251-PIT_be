package com.camote.app.repository;

import com.camote.app.model.Alerta;
import com.camote.app.model.Alerta.NivelRiesgo;
import com.camote.app.model.Alerta.EstadoAlerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la entidad Alerta.
 * Proporciona operaciones CRUD y consultas personalizadas para alertas de riesgo.
 */
@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    /**
     * Obtiene todas las alertas de una parcela específica.
     *
     * @param parcelaId ID de la parcela
     * @return lista de alertas de la parcela
     */
    List<Alerta> findByParcelaId(Long parcelaId);

    /**
     * Obtiene alertas no atendidas (pendientes) de una parcela.
     *
     * @param parcelaId ID de la parcela
     * @return lista de alertas pendientes
     */
    List<Alerta> findByParcelaIdAndEstado(Long parcelaId, EstadoAlerta estado);

    /**
     * Obtiene alertas por nivel de riesgo.
     *
     * @param nivelRiesgo nivel de riesgo
     * @return lista de alertas con ese nivel
     */
    List<Alerta> findByNivelRiesgo(NivelRiesgo nivelRiesgo);

    /**
     * Obtiene alertas críticas de una parcela.
     *
     * @param parcelaId ID de la parcela
     * @param nivel nivel de riesgo crítico
     * @return lista de alertas críticas
     */
    @Query("SELECT a FROM Alerta a WHERE a.parcela.id = :parcelaId AND a.nivelRiesgo = :nivel")
    List<Alerta> findAlertasCriticas(@Param("parcelaId") Long parcelaId, 
                                     @Param("nivel") NivelRiesgo nivel);

    /**
     * Obtiene alertas en un rango de fechas.
     *
     * @param fechaInicio fecha de inicio
     * @param fechaFin fecha de fin
     * @return lista de alertas en el rango
     */
    List<Alerta> findByFechaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    /**
     * Obtiene alertas pendientes de una parcela en un rango de fechas.
     *
     * @param parcelaId ID de la parcela
     * @param fechaInicio fecha de inicio
     * @param fechaFin fecha de fin
     * @return lista de alertas pendientes
     */
    @Query("SELECT a FROM Alerta a WHERE a.parcela.id = :parcelaId AND a.estado = 'PENDIENTE' AND a.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<Alerta> findAlertasPendientes(@Param("parcelaId") Long parcelaId, 
                                       @Param("fechaInicio") LocalDateTime fechaInicio, 
                                       @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Cuenta alertas críticas y altas sin atender.
     *
     * @param parcelaId ID de la parcela
     * @return cantidad de alertas críticas
     */
    @Query("SELECT COUNT(a) FROM Alerta a WHERE a.parcela.id = :parcelaId AND a.estado = 'PENDIENTE' AND (a.nivelRiesgo = 'CRITICA' OR a.nivelRiesgo = 'ALTA')")
    Long countAlertasGraves(@Param("parcelaId") Long parcelaId);
}
