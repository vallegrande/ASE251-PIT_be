package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "alertas")
@SQLRestriction("deleted = 0")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parcela_id", nullable = false)
    private Parcela parcela;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoAlerta tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private NivelAlerta nivel = NivelAlerta.MEDIA;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private EstadoAlerta estado = EstadoAlerta.PENDIENTE;

    @Column(name = "fecha_alerta")
    private LocalDateTime fechaAlerta;

    @Column(name = "fecha_atencion")
    private LocalDateTime fechaAtencion;

    @Column(nullable = false)
    private boolean deleted = false;

    @PrePersist
    protected void onCreate() {
        if (fechaAlerta == null) {
            fechaAlerta = LocalDateTime.now();
        }
    }

    public enum TipoAlerta {
        PLAGA, SEQUIA, LLUVIA_INTENSA, TEMPERATURA, OTRO;

        public String getLabel() {
            return switch (this) {
                case PLAGA -> "Plaga";
                case SEQUIA -> "Sequía";
                case LLUVIA_INTENSA -> "Lluvia Intensa";
                case TEMPERATURA -> "Temperatura";
                case OTRO -> "Otro";
            };
        }

        public String getIcono() {
            return switch (this) {
                case PLAGA -> "🐛";
                case SEQUIA -> "☀️";
                case LLUVIA_INTENSA -> "🌧️";
                case TEMPERATURA -> "🌡️";
                case OTRO -> "⚠️";
            };
        }
    }

    public enum NivelAlerta {
        BAJA, MEDIA, ALTA, CRITICA;

        public String getCssClass() {
            return switch (this) {
                case BAJA -> "nivel-baja";
                case MEDIA -> "nivel-media";
                case ALTA -> "nivel-alta";
                case CRITICA -> "nivel-critica";
            };
        }

        public String getLabel() {
            return switch (this) {
                case BAJA -> "Baja";
                case MEDIA -> "Media";
                case ALTA -> "Alta";
                case CRITICA -> "Crítica";
            };
        }
    }

    public enum EstadoAlerta {
        PENDIENTE, ATENDIDA, DESCARTADA;

        public String getLabel() {
            return switch (this) {
                case PENDIENTE -> "Pendiente";
                case ATENDIDA -> "Atendida";
                case DESCARTADA -> "Descartada";
            };
        }
    }
}
