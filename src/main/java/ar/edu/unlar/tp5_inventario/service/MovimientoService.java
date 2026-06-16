package ar.edu.unlar.tp5_inventario.service;

import ar.edu.unlar.tp5_inventario.dto.MovimientoRequest;
import ar.edu.unlar.tp5_inventario.dto.MovimientoResponse;
import java.util.List;

// Contrato para el servicio de gestión de movimientos (entradas/salidas).

public interface MovimientoService {

    MovimientoResponse registrarMovimiento(MovimientoRequest request);

    List<MovimientoResponse> obtenerHistorialPorProducto(Long productoId);
}