package com.example.demo.mapper;

import com.example.demo.dto.AlertaRequestDTO;
import com.example.demo.dto.AlertaResponseDTO;
import com.example.demo.model.Alerta;
import org.springframework.stereotype.Component;

@Component
public class AlertaMapper {

    public AlertaResponseDTO toDTO(Alerta alerta) {
        AlertaResponseDTO dto = new AlertaResponseDTO();
        dto.setId(alerta.getId());
        dto.setParcelaId(alerta.getParcela().getId());
        dto.setParcelaNombre(alerta.getParcela().getNombre());
        dto.setTipo(alerta.getTipo());
        dto.setNivel(alerta.getNivel());
        dto.setDescripcion(alerta.getDescripcion());
        dto.setEstado(alerta.getEstado());
        dto.setFechaAlerta(alerta.getFechaAlerta());
        dto.setFechaAtencion(alerta.getFechaAtencion());
        return dto;
    }

    public Alerta toEntity(AlertaRequestDTO dto) {
        Alerta alerta = new Alerta();
        alerta.setTipo(dto.getTipo());
        alerta.setNivel(dto.getNivel());
        alerta.setDescripcion(dto.getDescripcion());
        return alerta;
    }
}
