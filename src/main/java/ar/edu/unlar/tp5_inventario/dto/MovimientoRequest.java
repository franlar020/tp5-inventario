package ar.edu.unlar.tp5_inventario.dto;
import ar.edu.unlar.tp5_inventario.model.TipoMovimiento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// DTO para registrar un nuevo movimiento de inventario.

public record MovimientoRequest(
        @NotNull(message = "El ID del producto es obligatorio")
        Long productoId,

        @NotNull(message = "El tipo de movimiento es obligatorio")
        TipoMovimiento tipo,

        @Positive(message = "La cantidad debe ser mayor a 0")
        int cantidad,

        @NotBlank(message = "Debe especificar un motivo para el movimiento")
        String motivo
) {}