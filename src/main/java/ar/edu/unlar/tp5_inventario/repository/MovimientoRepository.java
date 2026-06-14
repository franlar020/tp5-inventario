package ar.edu.unlar.tp5_inventario.repository;

import ar.edu.unlar.tp5_inventario.model.MovimientoInventario;
import java.util.List;

// Contrato especifico para el repositorio de Movimientos de Inventario.
 
public interface MovimientoRepository extends IGenericRepository<MovimientoInventario, Long> {
    
    //Obtiene el historial completo de movimientos para un producto dado.
    
    List<MovimientoInventario> findByProductoId(Long productoId);
}