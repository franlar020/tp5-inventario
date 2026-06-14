package ar.edu.unlar.tp5_inventario.repository;

import ar.edu.unlar.tp5_inventario.model.Producto;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class InMemoryProductoRepository 
        extends GenericInMemoryRepository<Producto, Long> 
        implements ProductoRepository {

    @Override
    public List<Producto> findByCategoria(Long categoriaId) {
        return dataStore.values().stream()
                .filter(p -> p.getCategoria().getId().equals(categoriaId))
                .toList();
    }

    @Override
    public List<Producto> buscarPorNombre(String texto) {
        String lower = texto.toLowerCase();
        return dataStore.values().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(lower))
                .toList();
    }
}