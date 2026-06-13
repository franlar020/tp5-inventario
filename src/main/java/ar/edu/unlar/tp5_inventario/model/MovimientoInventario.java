package ar.edu.unlar.tp5_inventario.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoInventario {

    private Long id; // Identificador único del movimiento
    private Long productoId; // Referencia al producto afectado
    private TipoMovimiento tipo; // Tipo de movimiento: ENTRADA o SALIDA
    private int cantidad; // Cantidad de unidades movidas
    private int stockResultante; // Stock resultante después de aplicar el movimiento
    private String motivo; // Descripción opcional del motivo del movimiento
    private LocalDateTime fecha; // Fecha y hora en que se registró el movimiento

}