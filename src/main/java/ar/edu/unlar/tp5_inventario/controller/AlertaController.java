package ar.edu.unlar.tp5_inventario.controller;

import ar.edu.unlar.tp5_inventario.dto.AlertaStockResponse;
import ar.edu.unlar.tp5_inventario.service.AlertaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
public class AlertaController {

    private final AlertaService alertaService;

    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    @GetMapping("/stock-bajo")
    public ResponseEntity<List<AlertaStockResponse>> obtenerAlertas() {
        return ResponseEntity.ok(alertaService.obtenerProductosConStockBajo());
    }
}