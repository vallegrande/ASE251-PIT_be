package com.example.demo.dto;

import com.example.demo.model.Alerta.EstadoAlerta;
import com.example.demo.model.Alerta.NivelAlerta;
import com.example.demo.model.Alerta.TipoAlerta;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlertaResponseDTO {

    private Long id;
    private Long parcelaId;
    private String parcelaNombre;
    private TipoAlerta tipo;
    private NivelAlerta nivel;
    private String descripcion;
    private EstadoAlerta estado;
    private LocalDateTime fechaAlerta;
    private LocalDateTime fechaAtencion;
}
