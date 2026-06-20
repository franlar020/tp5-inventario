package ar.edu.unlar.tp5_inventario.dto;

import java.util.Map;

// DTO para estructurar el reporte de rendimiento del sistema
public record MetricaPerformance(
        String operacion,
        String complejidad,
        Map<String, Long> tiempos
) {}