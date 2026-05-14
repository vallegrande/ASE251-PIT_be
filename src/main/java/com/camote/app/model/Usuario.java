package com.camote.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa un usuario del sistema.
 * Los usuarios son responsables de gestionar una o más parcelas
 * y reciben alertas sobre condiciones de riesgo agrícola.
 */
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Column(nullable = false, length = 255)
    private String password;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank(message = "El nombre completo es obligatorio")
    @Column(nullable = false, length = 150)
    private String nombreCompleto;

    @Column(length = 20)
    private String telefono;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String direccion;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro;

    @Column
    private LocalDateTime fechaUltimaActividad;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RolUsuario rol = RolUsuario.AGRICULTOR;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoUsuario estado = EstadoUsuario.ACTIVO;

    /**
     * Relación uno-a-muchos con Parcela.
     * Un usuario puede poseer múltiples parcelas.
     */
    @Builder.Default
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("usuario")
    private Set<Parcela> parcelas = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }

    /**
     * Roles de usuario en el sistema.
     */
    public enum RolUsuario {
        ADMINISTRATIVO, AGRICULTOR, ESPECIALISTA
    }

    /**
     * Estados posibles de un usuario.
     */
    public enum EstadoUsuario {
        ACTIVO, INACTIVO, BLOQUEADO
    }
}
