package ar.edu.unlar.tp5_inventario.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

// DTO para la recepción de datos al crear o actualizar un Producto.

public record ProductoRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
        String nombre,

        @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
        String descripcion,

        @PositiveOrZero(message = "El precio debe ser >= 0")
        double precio,

        @PositiveOrZero(message = "El stock inicial debe ser >= 0")
        int stockInicial,

        @NotNull(message = "La categoría es obligatoria")
        Long categoriaId
) {}