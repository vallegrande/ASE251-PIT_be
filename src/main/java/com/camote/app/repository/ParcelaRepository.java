package com.camote.app.repository;

import com.camote.app.model.Parcela;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Parcela.
 * Proporciona operaciones CRUD y consultas personalizadas para parcelas.
 */
@Repository
public interface ParcelaRepository extends JpaRepository<Parcela, Long> {

    /**
     * Obtiene todas las parcelas de un usuario específico.
     *
     * @param usuarioId ID del usuario propietario
     * @return lista de parcelas del usuario
     */
    List<Parcela> findByUsuarioId(Long usuarioId);

    /**
     * Obtiene parcelas por nombre.
     *
     * @param nombre nombre de la parcela
     * @return lista de parcelas con ese nombre
     */
    List<Parcela> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Obtiene parcelas por estado.
     *
     * @param estado estado de la parcela
     * @return lista de parcelas con ese estado
     */
    List<Parcela> findByEstado(Parcela.EstadoParcela estado);

    /**
     * Obtiene una parcela por nombre y usuario.
     *
     * @param nombre nombre de la parcela
     * @param usuarioId ID del usuario propietario
     * @return Optional con la parcela si existe
     */
    Optional<Parcela> findByNombreAndUsuarioId(String nombre, Long usuarioId);

    /**
     * Cuenta las parcelas activas de un usuario.
     *
     * @param usuarioId ID del usuario
     * @param estado estado a contar
     * @return cantidad de parcelas
     */
    Long countByUsuarioIdAndEstado(Long usuarioId, Parcela.EstadoParcela estado);
}
