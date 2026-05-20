package com.example.demo.mapper;

import com.example.demo.dto.ParcelaRequestDTO;
import com.example.demo.dto.ParcelaResponseDTO;
import com.example.demo.model.Parcela;
import org.springframework.stereotype.Component;

@Component
public class ParcelaMapper {

    public ParcelaResponseDTO toDTO(Parcela parcela) {
        ParcelaResponseDTO dto = new ParcelaResponseDTO();
        dto.setId(parcela.getId());
        dto.setNombre(parcela.getNombre());
        dto.setArea(parcela.getArea());
        dto.setUbicacion(parcela.getUbicacion());
        dto.setEstado(parcela.getEstado());
        dto.setHumedad(parcela.getHumedad());
        dto.setTemperatura(parcela.getTemperatura());
        dto.setFechaSiembra(parcela.getFechaSiembra());
        dto.setFechaCosechaEstimada(parcela.getFechaCosechaEstimada());
        dto.setCreatedAt(parcela.getCreatedAt());
        dto.setUpdatedAt(parcela.getUpdatedAt());
        return dto;
    }

    public Parcela toEntity(ParcelaRequestDTO dto) {
        Parcela parcela = new Parcela();
        parcela.setNombre(dto.getNombre());
        parcela.setArea(dto.getArea());
        parcela.setUbicacion(dto.getUbicacion());
        parcela.setEstado(dto.getEstado());
        parcela.setHumedad(dto.getHumedad());
        parcela.setTemperatura(dto.getTemperatura());
        parcela.setFechaSiembra(dto.getFechaSiembra());
        parcela.setFechaCosechaEstimada(dto.getFechaCosechaEstimada());
        return parcela;
    }

    public void updateEntity(Parcela parcela, ParcelaRequestDTO dto) {
        parcela.setNombre(dto.getNombre());
        parcela.setArea(dto.getArea());
        parcela.setUbicacion(dto.getUbicacion());
        parcela.setEstado(dto.getEstado());
        parcela.setHumedad(dto.getHumedad());
        parcela.setTemperatura(dto.getTemperatura());
        parcela.setFechaSiembra(dto.getFechaSiembra());
        parcela.setFechaCosechaEstimada(dto.getFechaCosechaEstimada());
    }
}
