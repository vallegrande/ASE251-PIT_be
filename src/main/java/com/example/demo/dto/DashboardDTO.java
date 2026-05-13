package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {

    private long totalParcelas;
    private long parcelasActivas;
    private long parcelasEnRiesgo;
    private double areaTotalHectareas;
    private long alertasPendientes;
    private long alertasCriticas;
}
