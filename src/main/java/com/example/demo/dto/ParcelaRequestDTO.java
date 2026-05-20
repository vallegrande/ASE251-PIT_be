package com.example.demo.dto;

import com.example.demo.model.Parcela.EstadoParcela;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ParcelaRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El área es obligatoria")
    @DecimalMin(value = "0.01", message = "El área debe ser mayor a 0")
    private BigDecimal area;

    @NotBlank(message = "La ubicación es obligatoria")
    private String ubicacion;

    @NotNull(message = "El estado es obligatorio")
    private EstadoParcela estado;

    private BigDecimal humedad;
    private BigDecimal temperatura;
    private LocalDate fechaSiembra;
    private LocalDate fechaCosechaEstimada;
}
