package com.example.demo.repository;

import com.example.demo.model.Parcela;
import com.example.demo.model.Parcela.EstadoParcela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParcelaRepository extends JpaRepository<Parcela, Long> {

    List<Parcela> findByEstado(EstadoParcela estado);

    List<Parcela> findByNombreContainingIgnoreCase(String nombre);

    long countByEstado(EstadoParcela estado);

    @Query("SELECT COUNT(p) FROM Parcela p WHERE p.estado != 'INACTIVO'")
    long countActivas();

    @Query("SELECT SUM(p.area) FROM Parcela p WHERE p.estado != 'INACTIVO'")
    Double sumAreaActiva();
}
