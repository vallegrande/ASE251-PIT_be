package com.camote.app.repository;

import com.camote.app.model.Usuario;
import com.camote.app.model.Usuario.RolUsuario;
import com.camote.app.model.Usuario.EstadoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 * Proporciona operaciones CRUD y consultas personalizadas para usuarios del sistema.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Obtiene un usuario por su nombre de usuario.
     *
     * @param username nombre de usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Obtiene un usuario por su email.
     *
     * @param email email del usuario
     * @return Optional con el usuario si existe
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Obtiene usuarios por rol.
     *
     * @param rol rol del usuario
     * @return lista de usuarios con ese rol
     */
    List<Usuario> findByRol(RolUsuario rol);

    /**
     * Obtiene usuarios por estado.
     *
     * @param estado estado del usuario
     * @return lista de usuarios con ese estado
     */
    List<Usuario> findByEstado(EstadoUsuario estado);

    /**
     * Obtiene usuarios activos por rol.
     *
     * @param rol rol del usuario
     * @param estado estado del usuario
     * @return lista de usuarios con rol y estado especificados
     */
    List<Usuario> findByRolAndEstado(RolUsuario rol, EstadoUsuario estado);

    /**
     * Valida si existe un usuario con el username y email especificados.
     *
     * @param username nombre de usuario
     * @param email email del usuario
     * @return true si existe, false en caso contrario
     */
    boolean existsByUsernameOrEmail(String username, String email);

    /**
     * Cuenta usuarios activos.
     *
     * @return cantidad de usuarios activos
     */
    Long countByEstado(EstadoUsuario estado);
}
