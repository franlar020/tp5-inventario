package ar.edu.unlar.tp5_inventario.service;

import ar.edu.unlar.tp5_inventario.dto.MovimientoRequest;
import ar.edu.unlar.tp5_inventario.dto.MovimientoResponse;
import ar.edu.unlar.tp5_inventario.exception.InsufficientStockException;
import ar.edu.unlar.tp5_inventario.exception.ResourceNotFoundException;
import ar.edu.unlar.tp5_inventario.model.MovimientoInventario;
import ar.edu.unlar.tp5_inventario.model.Producto;
import ar.edu.unlar.tp5_inventario.model.TipoMovimiento;
import ar.edu.unlar.tp5_inventario.repository.MovimientoRepository;
import ar.edu.unlar.tp5_inventario.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementación del servicio de movimientos.
 *
 * @author Grupo 3 — Inventario Inteligente
 * @since 1.0
 */
@Service
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final ProductoRepository productoRepository;

    public MovimientoServiceImpl(MovimientoRepository movimientoRepository, ProductoRepository productoRepository) {
        this.movimientoRepository = movimientoRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public MovimientoResponse registrarMovimiento(MovimientoRequest request) {
        Producto producto = productoRepository.findById(request.productoId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto con ID: " + request.productoId()));

        int stockResultante;

        if (request.tipo() == TipoMovimiento.ENTRADA) {
            stockResultante = producto.incrementarStock(request.cantidad());
        } else {
            // Validar de forma preventiva
            if (producto.getStock() < request.cantidad()) {
                throw new InsufficientStockException("No se pueden retirar " + request.cantidad() 
                        + " unidades. Stock disponible: " + producto.getStock());
            }
            // Operación atómica de decremento
            stockResultante = producto.decrementarStock(request.cantidad());
            
            // Verificación post-decremento (por si otro hilo modificó concurrentemente)
            if (stockResultante < 0) {
                // Hacemos rollback lógico
                producto.incrementarStock(request.cantidad());
                throw new InsufficientStockException("Condición de carrera detectada: Stock insuficiente.");
            }
        }

        MovimientoInventario movimiento = new MovimientoInventario(
                null,
                producto.getId(),
                request.tipo(),
                request.cantidad(),
                stockResultante,
                request.motivo(),
                LocalDateTime.now()
        );

        MovimientoInventario guardado = movimientoRepository.save(movimiento);

        return new MovimientoResponse(
                guardado.getId(),
                guardado.getProductoId(),
                guardado.getTipo(),
                guardado.getCantidad(),
                guardado.getStockResultante(),
                guardado.getMotivo(),
                guardado.getFecha()
        );
    }

    @Override
    public List<MovimientoResponse> obtenerHistorialPorProducto(Long productoId) {
        // Validar que el producto exista antes de buscar su historial
        if (!productoRepository.existsById(productoId)) {
            throw new ResourceNotFoundException("No se encontró el producto con ID: " + productoId);
        }

        return movimientoRepository.findByProductoId(productoId).stream()
                .map(m -> new MovimientoResponse(
                        m.getId(), m.getProductoId(), m.getTipo(), m.getCantidad(),
                        m.getStockResultante(), m.getMotivo(), m.getFecha()
                )).toList();
    }
}