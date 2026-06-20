package ar.edu.unlar.tp5_inventario.controller;

import ar.edu.unlar.tp5_inventario.dto.MetricaPerformance;
import ar.edu.unlar.tp5_inventario.service.PerformanceReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final PerformanceReportService performanceReportService;

    public AdminController(PerformanceReportService performanceReportService) {
        this.performanceReportService = performanceReportService;
    }

    @GetMapping("/performance-report")
    public ResponseEntity<List<MetricaPerformance>> generarReporte() {
        return ResponseEntity.ok(performanceReportService.generarReporte());
    }
}