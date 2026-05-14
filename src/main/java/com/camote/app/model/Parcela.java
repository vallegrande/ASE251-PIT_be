package com.camote.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa una parcela (terreno agrícola) en el sistema.
 * Una parcela contiene información geográfica, características del suelo y cultivos,
 * así como el historial de monitoreos y alertas asociadas.
 */
@Entity
@Table(name = "parcelas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parcela {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la parcela es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "La ubicación es obligatoria")
    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String ubicacion;

    @NotNull(message = "El área es obligatoria")
    @Column(nullable = false)
    private Double area;

    @Column(length = 50)
    private String tipoSuelo;

    @Column(length = 100)
    private String cultivo;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column
    private LocalDateTime fechaUltimaModificacion;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoParcela estado = EstadoParcela.ACTIVA;

    /**
     * Relación uno-a-muchos con Monitoreo.
     * Una parcela puede tener múltiples monitoreos.
     */
    @Builder.Default
    @OneToMany(mappedBy = "parcela", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("parcela")
    private Set<Monitoreo> monitoreos = new HashSet<>();

    /**
     * Relación uno-a-muchos con Alerta.
     * Una parcela puede tener múltiples alertas asociadas.
     */
    @Builder.Default
    @OneToMany(mappedBy = "parcela", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("parcela")
    private Set<Alerta> alertas = new HashSet<>();

    /**
     * Relación muchos-a-uno con Usuario.
     * Una parcela pertenece a un usuario específico.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties("parcelas")
    private Usuario usuario;

    @PrePersist
    protected void onCreate() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
        if (fechaUltimaModificacion == null) {
            fechaUltimaModificacion = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaUltimaModificacion = LocalDateTime.now();
    }

    /**
     * Estados posibles de una parcela.
     */
    public enum EstadoParcela {
        ACTIVA, INACTIVA, BAJO_MANTENIMIENTO
    }
}
