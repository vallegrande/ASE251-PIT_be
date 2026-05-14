package com.camote.app.repository;

import com.camote.app.model.Monitoreo;
import com.camote.app.model.Monitoreo.EstadoMonitoreo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la entidad Monitoreo.
 * Proporciona operaciones CRUD y consultas personalizadas para registros de monitoreo.
 */
@Repository
public interface MonitoreoRepository extends JpaRepository<Monitoreo, Long> {

    /**
     * Obtiene todos los monitoreos de una parcela específica.
     *
     * @param parcelaId ID de la parcela
     * @return lista de monitoreos de la parcela
     */
    List<Monitoreo> findByParcelaId(Long parcelaId);

    /**
     * Obtiene monitoreos de una parcela en un rango de fechas.
     *
     * @param parcelaId ID de la parcela
     * @param fechaInicio fecha de inicio
     * @param fechaFin fecha de fin
     * @return lista de monitoreos en el rango
     */
    @Query("SELECT m FROM Monitoreo m WHERE m.parcela.id = :parcelaId AND m.fechaMonitoreo BETWEEN :fechaInicio AND :fechaFin")
    List<Monitoreo> findMonitoreosPorFecha(@Param("parcelaId") Long parcelaId, 
                                           @Param("fechaInicio") LocalDateTime fechaInicio, 
                                           @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Obtiene el monitoreo más reciente de una parcela.
     *
     * @param parcelaId ID de la parcela
     * @return el monitoreo más reciente
     */
    @Query(value = "SELECT TOP 1 * FROM monitoreos WHERE parcela_id = :parcelaId ORDER BY fecha_monitoreo DESC", nativeQuery = true)
    Monitoreo findUltimoMonitoreo(@Param("parcelaId") Long parcelaId);

    /**
     * Obtiene monitoreos por estado.
     *
     * @param estado estado del monitoreo
     * @return lista de monitoreos con ese estado
     */
    List<Monitoreo> findByEstado(EstadoMonitoreo estado);

    /**
     * Obtiene monitoreos completados de una parcela.
     *
     * @param parcelaId ID de la parcela
     * @return lista de monitoreos completados
     */
    List<Monitoreo> findByParcelaIdAndEstado(Long parcelaId, EstadoMonitoreo estado);

    /**
     * Cuenta monitoreos de una parcela.
     *
     * @param parcelaId ID de la parcela
     * @return cantidad de monitoreos
     */
    Long countByParcelaId(Long parcelaId);
}
