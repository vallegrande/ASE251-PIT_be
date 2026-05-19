package com.camote.app.rest;

import com.camote.app.model.Usuario;
import com.camote.app.model.Usuario.EstadoUsuario;
import com.camote.app.model.Usuario.RolUsuario;
import com.camote.app.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * Controlador REST para gestionar operaciones de Usuarios.
 * Proporciona endpoints para autenticación, autorización y gestión de perfiles de usuario.
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class UsuarioRestController {

    private final UsuarioService usuarioService;

    /**
     * Obtiene todos los usuarios.
     *
     * @return lista de usuarios
     */
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerTodos() {
        log.info("GET /api/usuarios - Obteniendo todos los usuarios");
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }

    /**
     * Obtiene un usuario por ID.
     *
     * @param id ID del usuario
     * @return usuario encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable @NonNull Long id) {
        Objects.requireNonNull(id);
        log.info("GET /api/usuarios/{} - Obteniendo usuario", id);
        return usuarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene un usuario por username.
     *
     * @param username nombre de usuario
     * @return usuario encontrado
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<Usuario> obtenerPorUsername(@PathVariable String username) {
        log.info("GET /api/usuarios/username/{} - Obteniendo usuario por username", username);
        return usuarioService.obtenerPorUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene un usuario por email.
     *
     * @param email email del usuario
     * @return usuario encontrado
     */
    @GetMapping("/email")
    public ResponseEntity<Usuario> obtenerPorEmail(@RequestParam String email) {
        log.info("GET /api/usuarios/email?email={} - Obteniendo usuario por email", email);
        return usuarioService.obtenerPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene usuarios por rol.
     *
     * @param rol rol del usuario
     * @return lista de usuarios
     */
    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<Usuario>> obtenerPorRol(@PathVariable RolUsuario rol) {
        log.info("GET /api/usuarios/rol/{} - Obteniendo usuarios por rol", rol);
        return ResponseEntity.ok(usuarioService.obtenerPorRol(rol));
    }

    /**
     * Obtiene usuarios por estado.
     *
     * @param estado estado del usuario
     * @return lista de usuarios
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Usuario>> obtenerPorEstado(@PathVariable EstadoUsuario estado) {
        log.info("GET /api/usuarios/estado/{} - Obteniendo usuarios por estado", estado);
        return ResponseEntity.ok(usuarioService.obtenerPorEstado(estado));
    }

    /**
     * Obtiene usuarios activos por rol.
     *
     * @param rol rol del usuario
     * @return lista de usuarios activos
     */
    @GetMapping("/rol/{rol}/activos")
    public ResponseEntity<List<Usuario>> obtenerActivosPorRol(@PathVariable RolUsuario rol) {
        log.info("GET /api/usuarios/rol/{}/activos - Obteniendo usuarios activos por rol", rol);
        return ResponseEntity.ok(usuarioService.obtenerActivosPorRol(rol));
    }

    /**
     * Crea un nuevo usuario.
     *
     * @param usuario datos del usuario
     * @return usuario creado
     */
    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody Usuario usuario) {
        log.info("POST /api/usuarios - Creando nuevo usuario: {}", usuario.getUsername());
        try {
            Usuario usuarioCreado = usuarioService.crear(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
        } catch (IllegalArgumentException e) {
            log.error("Error al crear usuario: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza un usuario existente.
     *
     * @param id ID del usuario
     * @param usuario datos actualizados
     * @return usuario actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable @NonNull Long id, @RequestBody Usuario usuario) {
        Objects.requireNonNull(id);
        log.info("PUT /api/usuarios/{} - Actualizando usuario", id);
        return usuarioService.actualizar(id, usuario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cambia la contraseña de un usuario.
     *
     * @param id ID del usuario
     * @param nuevaPassword nueva contraseña
     * @return usuario actualizado
     */
    @PatchMapping("/{id}/password")
    public ResponseEntity<Usuario> cambiarPassword(@PathVariable @NonNull Long id, @RequestParam String nuevaPassword) {
        Objects.requireNonNull(id);
        log.info("PATCH /api/usuarios/{}/password - Cambiando contraseña", id);
        return usuarioService.cambiarPassword(id, nuevaPassword)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cambia el estado de un usuario.
     *
     * @param id ID del usuario
     * @param estado nuevo estado
     * @return usuario actualizado
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Usuario> cambiarEstado(@PathVariable @NonNull Long id, @RequestParam EstadoUsuario estado) {
        Objects.requireNonNull(id);
        log.info("PATCH /api/usuarios/{}/estado - Cambiando estado a {}", id, estado);
        return usuarioService.cambiarEstado(id, estado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un usuario.
     *
     * @param id ID del usuario
     * @return respuesta sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable @NonNull Long id) {
        Objects.requireNonNull(id);
        log.info("DELETE /api/usuarios/{} - Eliminando usuario", id);
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtiene el conteo de usuarios activos.
     *
     * @return cantidad de usuarios activos
     */
    @GetMapping("/activos/contar")
    public ResponseEntity<Long> contarActivos() {
        log.info("GET /api/usuarios/activos/contar - Contando usuarios activos");
        return ResponseEntity.ok(usuarioService.contarActivos());
    }
}
