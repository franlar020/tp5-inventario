package ar.edu.unlar.tp5_inventario.dto;
import ar.edu.unlar.tp5_inventario.model.TipoMovimiento;
import java.time.LocalDateTime;

// DTO para la exposición del historial de movimientos
public record MovimientoResponse(
        Long id,
        Long productoId,
        TipoMovimiento tipo,
        int cantidad,
        int stockResultante,
        String motivo,
        LocalDateTime fecha
) {}