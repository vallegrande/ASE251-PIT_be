package com.example.demo.service;

import com.example.demo.model.Parcela;
import com.example.demo.model.Parcela.EstadoParcela;

import java.util.List;
import java.util.Optional;

public interface ParcelaService {

    List<Parcela> findAll();

    Optional<Parcela> findById(Long id);

    Parcela save(Parcela parcela);

    void deleteById(Long id);

    List<Parcela> findByEstado(EstadoParcela estado);

    List<Parcela> findByNombre(String nombre);

    long countActivas();

    long countEnRiesgo();

    Double sumAreaActiva();

    void restoreById(Long id);

    List<Parcela> findDeleted();
}
