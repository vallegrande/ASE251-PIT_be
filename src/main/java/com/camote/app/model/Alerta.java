package com.camote.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa una alerta de riesgo agrícola asociada a una parcela.
 */
@Entity
@Table(name = "alertas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El tipo de alerta es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoAlerta tipo;

    @NotBlank(message = "El mensaje es obligatorio")
    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String mensaje;

    @NotNull(message = "El nivel de riesgo es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_riesgo", nullable = false, length = 10)
    private NivelRiesgo nivelRiesgo = NivelRiesgo.MEDIA;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private EstadoAlerta estado = EstadoAlerta.PENDIENTE;

    /** Parcela asociada a esta alerta */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parcela_id", nullable = false)
    @JsonIgnoreProperties({"alertas", "monitoreos"})
    private Parcela parcela;

    @PrePersist
    protected void onCreate() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
    }

    /** Tipos de alerta de riesgo agrícola */
    public enum TipoAlerta {
        PLAGA, SEQUIA, LLUVIA_INTENSA, CALOR_EXCESIVO, HUMEDAD_BAJA, ENFERMEDAD, OTRO
    }

    /** Niveles de riesgo */
    public enum NivelRiesgo {
        BAJA, MEDIA, ALTA, CRITICA
    }

    /** Estados de la alerta */
    public enum EstadoAlerta {
        PENDIENTE, ATENDIDA, DESCARTADA
    }
}
