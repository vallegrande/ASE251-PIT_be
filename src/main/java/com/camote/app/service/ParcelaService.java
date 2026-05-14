package com.camote.app.service;

import com.camote.app.model.Parcela;
import com.camote.app.model.Parcela.EstadoParcela;
import com.camote.app.repository.ParcelaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar operaciones relacionadas con Parcelas.
 * Implementa la lógica de negocio para crear, actualizar, eliminar y consultar parcelas.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ParcelaService {

    private final ParcelaRepository parcelaRepository;

    /**
     * Obtiene todas las parcelas del sistema.
     *
     * @return lista de todas las parcelas
     */
    @Transactional(readOnly = true)
    public List<Parcela> obtenerTodas() {
        log.info("Obteniendo todas las parcelas");
        return parcelaRepository.findAll();
    }

    /**
     * Obtiene una parcela por su ID.
     *
     * @param id ID de la parcela
     * @return Optional con la parcela si existe
     */
    @Transactional(readOnly = true)
    public Optional<Parcela> obtenerPorId(Long id) {
        log.info("Obteniendo parcela con ID: {}", id);
        return parcelaRepository.findById(id);
    }

    /**
     * Obtiene todas las parcelas de un usuario.
     *
     * @param usuarioId ID del usuario
     * @return lista de parcelas del usuario
     */
    @Transactional(readOnly = true)
    public List<Parcela> obtenerPorUsuario(Long usuarioId) {
        log.info("Obteniendo parcelas del usuario: {}", usuarioId);
        return parcelaRepository.findByUsuarioId(usuarioId);
    }

    /**
     * Busca parcelas por nombre.
     *
     * @param nombre nombre a buscar
     * @return lista de parcelas encontradas
     */
    @Transactional(readOnly = true)
    public List<Parcela> buscarPorNombre(String nombre) {
        log.info("Buscando parcelas por nombre: {}", nombre);
        return parcelaRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Obtiene parcelas por estado.
     *
     * @param estado estado de la parcela
     * @return lista de parcelas con ese estado
     */
    @Transactional(readOnly = true)
    public List<Parcela> obtenerPorEstado(EstadoParcela estado) {
        log.info("Obteniendo parcelas por estado: {}", estado);
        return parcelaRepository.findByEstado(estado);
    }

    /**
     * Crea una nueva parcela.
     *
     * @param parcela datos de la parcela a crear
     * @return parcela creada
     */
    public Parcela crear(Parcela parcela) {
        log.info("Creando nueva parcela: {}", parcela.getNombre());
        parcela.setFechaCreacion(LocalDateTime.now());
        parcela.setFechaUltimaModificacion(LocalDateTime.now());
        parcela.setEstado(EstadoParcela.ACTIVA);
        return parcelaRepository.save(parcela);
    }

    /**
     * Actualiza una parcela existente.
     *
     * @param id ID de la parcela a actualizar
     * @param parcelaActualizada datos actualizados
     * @return parcela actualizada
     */
    public Optional<Parcela> actualizar(Long id, Parcela parcelaActualizada) {
        log.info("Actualizando parcela con ID: {}", id);
        return parcelaRepository.findById(id).map(parcela -> {
            parcela.setNombre(parcelaActualizada.getNombre());
            parcela.setUbicacion(parcelaActualizada.getUbicacion());
            parcela.setArea(parcelaActualizada.getArea());
            parcela.setTipoSuelo(parcelaActualizada.getTipoSuelo());
            parcela.setCultivo(parcelaActualizada.getCultivo());
            parcela.setDescripcion(parcelaActualizada.getDescripcion());
            parcela.setEstado(parcelaActualizada.getEstado());
            parcela.setFechaUltimaModificacion(LocalDateTime.now());
            return parcelaRepository.save(parcela);
        });
    }

    /**
     * Cambia el estado de una parcela.
     *
     * @param id ID de la parcela
     * @param nuevoEstado nuevo estado
     * @return parcela con estado actualizado
     */
    public Optional<Parcela> cambiarEstado(Long id, EstadoParcela nuevoEstado) {
        log.info("Cambiando estado de parcela {} a {}", id, nuevoEstado);
        return parcelaRepository.findById(id).map(parcela -> {
            parcela.setEstado(nuevoEstado);
            parcela.setFechaUltimaModificacion(LocalDateTime.now());
            return parcelaRepository.save(parcela);
        });
    }

    /**
     * Elimina una parcela.
     *
     * @param id ID de la parcela a eliminar
     */
    public void eliminar(Long id) {
        log.info("Eliminando parcela con ID: {}", id);
        parcelaRepository.deleteById(id);
    }

    /**
     * Cuenta las parcelas activas de un usuario.
     *
     * @param usuarioId ID del usuario
     * @return cantidad de parcelas activas
     */
    @Transactional(readOnly = true)
    public Long contarActivasPorUsuario(Long usuarioId) {
        log.info("Contando parcelas activas del usuario: {}", usuarioId);
        return parcelaRepository.countByUsuarioIdAndEstado(usuarioId, EstadoParcela.ACTIVA);
    }
}
