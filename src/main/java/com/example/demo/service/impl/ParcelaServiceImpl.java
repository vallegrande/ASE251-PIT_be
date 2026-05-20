package com.example.demo.service.impl;

import com.example.demo.model.Parcela;
import com.example.demo.model.Parcela.EstadoParcela;
import com.example.demo.repository.ParcelaRepository;
import com.example.demo.service.ParcelaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ParcelaServiceImpl implements ParcelaService {

    private final ParcelaRepository parcelaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Parcela> findAll() {
        return parcelaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Parcela> findById(Long id) {
        return parcelaRepository.findById(id);
    }

    @Override
    public Parcela save(Parcela parcela) {
        return parcelaRepository.save(parcela);
    }

    @Override
    public void deleteById(Long id) {
        parcelaRepository.softDeleteById(id);
    }

    @Override
    public void restoreById(Long id) {
        parcelaRepository.restoreById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Parcela> findDeleted() {
        return parcelaRepository.findDeleted();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Parcela> findByEstado(EstadoParcela estado) {
        return parcelaRepository.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Parcela> findByNombre(String nombre) {
        return parcelaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public long countActivas() {
        return parcelaRepository.countActivas();
    }

    @Override
    @Transactional(readOnly = true)
    public long countEnRiesgo() {
        return parcelaRepository.countByEstado(EstadoParcela.EN_RIESGO);
    }

    @Override
    @Transactional(readOnly = true)
    public Double sumAreaActiva() {
        Double total = parcelaRepository.sumAreaActiva();
        return total != null ? total : 0.0;
    }
}
