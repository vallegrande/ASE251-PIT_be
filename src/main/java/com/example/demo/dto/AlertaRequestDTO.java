package com.example.demo.dto;

import com.example.demo.model.Alerta.NivelAlerta;
import com.example.demo.model.Alerta.TipoAlerta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AlertaRequestDTO {

    @NotNull(message = "La parcela es obligatoria")
    private Long parcelaId;

    @NotNull(message = "El tipo es obligatorio")
    private TipoAlerta tipo;

    @NotNull(message = "El nivel es obligatorio")
    private NivelAlerta nivel;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
}
