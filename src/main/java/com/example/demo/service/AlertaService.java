package com.example.demo.service;

import com.example.demo.model.Alerta;
import com.example.demo.model.Alerta.EstadoAlerta;

import java.util.List;
import java.util.Optional;

public interface AlertaService {

    List<Alerta> findAll();

    Optional<Alerta> findById(Long id);

    Alerta save(Alerta alerta);

    void deleteById(Long id);

    List<Alerta> findPendientes();

    List<Alerta> findByParcelaId(Long parcelaId);

    Alerta atender(Long id);

    Alerta descartar(Long id);

    long countPendientes();

    long countCriticas();
}
