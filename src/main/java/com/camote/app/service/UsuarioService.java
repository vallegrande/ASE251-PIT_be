package com.camote.app.service;

import com.camote.app.model.Usuario;
import com.camote.app.model.Usuario.EstadoUsuario;
import com.camote.app.model.Usuario.RolUsuario;
import com.camote.app.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar operaciones relacionadas con Usuarios.
 * Implementa la lógica de negocio para autenticación, autorización y gestión de perfiles.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Obtiene todos los usuarios del sistema.
     *
     * @return lista de todos los usuarios
     */
    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodos() {
        log.info("Obteniendo todos los usuarios");
        return usuarioRepository.findAll();
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario
     * @return Optional con el usuario si existe
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerPorId(Long id) {
        log.info("Obteniendo usuario con ID: {}", id);
        return usuarioRepository.findById(id);
    }

    /**
     * Obtiene un usuario por su nombre de usuario (username).
     *
     * @param username nombre de usuario
     * @return Optional con el usuario si existe
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerPorUsername(String username) {
        log.info("Obteniendo usuario por username: {}", username);
        return usuarioRepository.findByUsername(username);
    }

    /**
     * Obtiene un usuario por su email.
     *
     * @param email email del usuario
     * @return Optional con el usuario si existe
     */
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerPorEmail(String email) {
        log.info("Obteniendo usuario por email: {}", email);
        return usuarioRepository.findByEmail(email);
    }

    /**
     * Obtiene usuarios por rol.
     *
     * @param rol rol del usuario
     * @return lista de usuarios con ese rol
     */
    @Transactional(readOnly = true)
    public List<Usuario> obtenerPorRol(RolUsuario rol) {
        log.info("Obteniendo usuarios por rol: {}", rol);
        return usuarioRepository.findByRol(rol);
    }

    /**
     * Obtiene usuarios por estado.
     *
     * @param estado estado del usuario
     * @return lista de usuarios con ese estado
     */
    @Transactional(readOnly = true)
    public List<Usuario> obtenerPorEstado(EstadoUsuario estado) {
        log.info("Obteniendo usuarios por estado: {}", estado);
        return usuarioRepository.findByEstado(estado);
    }

    /**
     * Obtiene usuarios activos por rol.
     *
     * @param rol rol del usuario
     * @return lista de usuarios activos con ese rol
     */
    @Transactional(readOnly = true)
    public List<Usuario> obtenerActivosPorRol(RolUsuario rol) {
        log.info("Obteniendo usuarios activos por rol: {}", rol);
        return usuarioRepository.findByRolAndEstado(rol, EstadoUsuario.ACTIVO);
    }

    /**
     * Crea un nuevo usuario.
     *
     * @param usuario datos del usuario a crear
     * @return usuario creado
     */
    public Usuario crear(Usuario usuario) {
        log.info("Creando nuevo usuario: {}", usuario.getUsername());
        if (usuarioRepository.existsByUsernameOrEmail(usuario.getUsername(), usuario.getEmail())) {
            throw new IllegalArgumentException("El username o email ya existe");
        }
        usuario.setFechaRegistro(LocalDateTime.now());
        usuario.setEstado(EstadoUsuario.ACTIVO);
        usuario.setRol(RolUsuario.AGRICULTOR);
        return usuarioRepository.save(usuario);
    }

    /**
     * Actualiza un usuario existente.
     *
     * @param id ID del usuario a actualizar
     * @param usuarioActualizado datos actualizados
     * @return usuario actualizado
     */
    public Optional<Usuario> actualizar(Long id, Usuario usuarioActualizado) {
        log.info("Actualizando usuario con ID: {}", id);
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombreCompleto(usuarioActualizado.getNombreCompleto());
            usuario.setEmail(usuarioActualizado.getEmail());
            usuario.setTelefono(usuarioActualizado.getTelefono());
            usuario.setDireccion(usuarioActualizado.getDireccion());
            usuario.setRol(usuarioActualizado.getRol());
            usuario.setEstado(usuarioActualizado.getEstado());
            usuario.setFechaUltimaActividad(LocalDateTime.now());
            return usuarioRepository.save(usuario);
        });
    }

    /**
     * Cambia la contraseña de un usuario.
     *
     * @param id ID del usuario
     * @param novaPassword nueva contraseña
     * @return usuario con contraseña actualizada
     */
    public Optional<Usuario> cambiarPassword(Long id, String novaPassword) {
        log.info("Cambiando contraseña del usuario: {}", id);
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setPassword(novaPassword); // En producción, esto debe ser hasheado
            usuario.setFechaUltimaActividad(LocalDateTime.now());
            return usuarioRepository.save(usuario);
        });
    }

    /**
     * Cambia el estado de un usuario.
     *
     * @param id ID del usuario
     * @param nuevoEstado nuevo estado
     * @return usuario con estado actualizado
     */
    public Optional<Usuario> cambiarEstado(Long id, EstadoUsuario nuevoEstado) {
        log.info("Cambiando estado del usuario {} a {}", id, nuevoEstado);
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setEstado(nuevoEstado);
            usuario.setFechaUltimaActividad(LocalDateTime.now());
            return usuarioRepository.save(usuario);
        });
    }

    /**
     * Actualiza la fecha de última actividad del usuario.
     *
     * @param id ID del usuario
     */
    public void actualizarUltimaActividad(Long id) {
        usuarioRepository.findById(id).ifPresent(usuario -> {
            usuario.setFechaUltimaActividad(LocalDateTime.now());
            usuarioRepository.save(usuario);
        });
    }

    /**
     * Elimina un usuario.
     *
     * @param id ID del usuario a eliminar
     */
    public void eliminar(Long id) {
        log.info("Eliminando usuario con ID: {}", id);
        usuarioRepository.deleteById(id);
    }

    /**
     * Cuenta usuarios activos en el sistema.
     *
     * @return cantidad de usuarios activos
     */
    @Transactional(readOnly = true)
    public Long contarActivos() {
        log.info("Contando usuarios activos");
        return usuarioRepository.countByEstado(EstadoUsuario.ACTIVO);
    }
}
