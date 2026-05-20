package com.example.demo.repository;

import com.example.demo.model.Parcela;
import com.example.demo.model.Parcela.EstadoParcela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query("UPDATE Parcela p SET p.deleted = true WHERE p.id = :id")
    void softDeleteById(Long id);

    @Modifying
    @Query(value = "UPDATE parcelas SET deleted = false WHERE id = :id", nativeQuery = true)
    void restoreById(Long id);

    @Query(value = "SELECT * FROM parcelas WHERE deleted = 1", nativeQuery = true)
    List<Parcela> findDeleted();
}
