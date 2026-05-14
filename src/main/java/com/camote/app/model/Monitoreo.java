package com.camote.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa un evento de monitoreo de una parcela.
 * Registra mediciones periódicas de variables ambientales y del cultivo
 * que sirven para detectar condiciones de riesgo.
 */
@Entity
@Table(name = "monitoreos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Monitoreo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha de monitoreo es obligatoria")
    @Column(nullable = false)
    private LocalDateTime fechaMonitoreo;

    @Column
    private Double temperatura;

    @Column
    private Double humedad;

    @Column
    private Double precipitacion;

    @Column
    private Double velocidadViento;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String observaciones;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoMonitoreo estado = EstadoMonitoreo.COMPLETADO;

    /**
     * Relación muchos-a-uno con Parcela.
     * Múltiples monitoreos pueden registrarse para una misma parcela.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parcela_id", nullable = false)
    @JsonIgnoreProperties({"monitoreos", "alertas"})
    private Parcela parcela;

    @PrePersist
    protected void onCreate() {
        if (fechaMonitoreo == null) {
            fechaMonitoreo = LocalDateTime.now();
        }
    }

    /**
     * Estados posibles de un monitoreo.
     */
    public enum EstadoMonitoreo {
        PENDIENTE, COMPLETADO, CANCELADO
    }
}
