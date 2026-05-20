package com.example.demo.repository;

import com.example.demo.model.Alerta;
import com.example.demo.model.Alerta.EstadoAlerta;
import com.example.demo.model.Alerta.NivelAlerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    List<Alerta> findByEstadoOrderByFechaAlertaDesc(EstadoAlerta estado);

    @Query("SELECT a FROM Alerta a JOIN FETCH a.parcela WHERE a.parcela.id = :parcelaId ORDER BY a.fechaAlerta DESC")
    List<Alerta> findByParcelaIdOrderByFechaAlertaDesc(Long parcelaId);

    @Query("SELECT a FROM Alerta a JOIN FETCH a.parcela ORDER BY a.fechaAlerta DESC")
    List<Alerta> findAllByOrderByFechaAlertaDesc();

    long countByEstado(EstadoAlerta estado);

    long countByNivel(NivelAlerta nivel);

    @Query("SELECT a FROM Alerta a JOIN FETCH a.parcela WHERE a.estado = 'PENDIENTE' ORDER BY a.nivel DESC, a.fechaAlerta DESC")
    List<Alerta> findPendientesOrdenadas();

    @Query("SELECT COUNT(a) FROM Alerta a WHERE a.estado = 'PENDIENTE' AND a.nivel IN ('ALTA', 'CRITICA')")
    long countCriticas();

    @Modifying
    @Query("UPDATE Alerta a SET a.deleted = true WHERE a.id = :id")
    void softDeleteById(Long id);

    @Modifying
    @Query(value = "UPDATE alertas SET deleted = false WHERE id = :id", nativeQuery = true)
    void restoreById(Long id);

    @Query(value = "SELECT * FROM alertas WHERE deleted = 1", nativeQuery = true)
    List<Alerta> findDeleted();
}
