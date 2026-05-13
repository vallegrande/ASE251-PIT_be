package com.example.demo.repository;

import com.example.demo.model.Alerta;
import com.example.demo.model.Alerta.EstadoAlerta;
import com.example.demo.model.Alerta.NivelAlerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    List<Alerta> findByEstadoOrderByFechaAlertaDesc(EstadoAlerta estado);

    List<Alerta> findByParcelaIdOrderByFechaAlertaDesc(Long parcelaId);

    List<Alerta> findAllByOrderByFechaAlertaDesc();

    long countByEstado(EstadoAlerta estado);

    long countByNivel(NivelAlerta nivel);

    @Query("SELECT a FROM Alerta a WHERE a.estado = 'PENDIENTE' ORDER BY a.nivel DESC, a.fechaAlerta DESC")
    List<Alerta> findPendientesOrdenadas();

    @Query("SELECT COUNT(a) FROM Alerta a WHERE a.estado = 'PENDIENTE' AND a.nivel IN ('ALTA', 'CRITICA')")
    long countCriticas();
}
