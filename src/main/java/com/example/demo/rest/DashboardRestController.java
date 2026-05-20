package com.example.demo.rest;

import com.example.demo.dto.DashboardDTO;
import com.example.demo.service.AlertaService;
import com.example.demo.service.ParcelaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardRestController {

    private final ParcelaService parcelaService;
    private final AlertaService alertaService;

    @GetMapping
    public ResponseEntity<DashboardDTO> resumen() {
        DashboardDTO dto = new DashboardDTO(
                parcelaService.findAll().size(),
                parcelaService.countActivas(),
                parcelaService.countEnRiesgo(),
                parcelaService.sumAreaActiva(),
                alertaService.countPendientes(),
                alertaService.countCriticas()
        );
        return ResponseEntity.ok(dto);
    }
}
