package ar.edu.unlar.tp5_inventario.repository;


import ar.edu.unlar.tp5_inventario.model.Producto;
import java.util.List;

// Contrato específico para el repositorio de Productos.

public interface ProductoRepository extends IGenericRepository<Producto, Long> {
    List<Producto> findByCategoria(Long categoriaId); // Filtra los productos que pertenecen a una categoría específica.
    List<Producto> buscarPorNombre(String texto); // Realiza una búsqueda textual (case-insensitive) sobre el nombre de los productos.
}