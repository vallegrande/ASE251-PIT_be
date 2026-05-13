package com.example.demo.dto;

import com.example.demo.model.Parcela.EstadoParcela;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ParcelaResponseDTO {

    private Long id;
    private String nombre;
    private BigDecimal area;
    private String ubicacion;
    private EstadoParcela estado;
    private BigDecimal humedad;
    private BigDecimal temperatura;
    private LocalDate fechaSiembra;
    private LocalDate fechaCosechaEstimada;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
