package com.example.demo.service.impl;

import com.example.demo.model.Alerta;
import com.example.demo.model.Alerta.EstadoAlerta;
import com.example.demo.repository.AlertaRepository;
import com.example.demo.service.AlertaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertaServiceImpl implements AlertaService {

    private final AlertaRepository alertaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Alerta> findAll() {
        return alertaRepository.findAllByOrderByFechaAlertaDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Alerta> findById(Long id) {
        return alertaRepository.findById(id);
    }

    @Override
    public Alerta save(Alerta alerta) {
        return alertaRepository.save(alerta);
    }

    @Override
    public void deleteById(Long id) {
        alertaRepository.softDeleteById(id);
    }

    @Override
    public void restoreById(Long id) {
        alertaRepository.restoreById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Alerta> findDeleted() {
        return alertaRepository.findDeleted();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Alerta> findPendientes() {
        return alertaRepository.findPendientesOrdenadas();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Alerta> findByParcelaId(Long parcelaId) {
        return alertaRepository.findByParcelaIdOrderByFechaAlertaDesc(parcelaId);
    }

    @Override
    public Alerta atender(Long id) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada con id: " + id));
        alerta.setEstado(EstadoAlerta.ATENDIDA);
        alerta.setFechaAtencion(LocalDateTime.now());
        return alertaRepository.save(alerta);
    }

    @Override
    public Alerta descartar(Long id) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alerta no encontrada con id: " + id));
        alerta.setEstado(EstadoAlerta.DESCARTADA);
        alerta.setFechaAtencion(LocalDateTime.now());
        return alertaRepository.save(alerta);
    }

    @Override
    @Transactional(readOnly = true)
    public long countPendientes() {
        return alertaRepository.countByEstado(EstadoAlerta.PENDIENTE);
    }

    @Override
    @Transactional(readOnly = true)
    public long countCriticas() {
        return alertaRepository.countCriticas();
    }
}
