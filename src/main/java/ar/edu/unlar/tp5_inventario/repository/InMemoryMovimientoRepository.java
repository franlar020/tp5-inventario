package ar.edu.unlar.tp5_inventario.repository;

import ar.edu.unlar.tp5_inventario.model.MovimientoInventario;
import org.springframework.stereotype.Repository;
import java.util.Comparator;
import java.util.List;

//Implementación en memoria del repositorio de Movimientos.
 
@Repository
public class InMemoryMovimientoRepository 
        extends GenericInMemoryRepository<MovimientoInventario, Long> 
        implements MovimientoRepository {

    @Override
    public List<MovimientoInventario> findByProductoId(Long productoId) {
        return dataStore.values().stream()
                .filter(m -> m.getProductoId().equals(productoId))
                // Ordenamos por fecha descendente (más recientes primero)
                .sorted(Comparator.comparing(MovimientoInventario::getFecha).reversed())
                .toList();
    }
}