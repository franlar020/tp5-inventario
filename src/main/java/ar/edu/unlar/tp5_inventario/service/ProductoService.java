package ar.edu.unlar.tp5_inventario.service;

import ar.edu.unlar.tp5_inventario.dto.ProductoRequest;
import ar.edu.unlar.tp5_inventario.dto.ProductoResponse;
import java.util.List;

// Contrato para el servicio de gestión de productos del inventario.

public interface ProductoService {

    List<ProductoResponse> obtenerTodos(Long categoriaId, Double precioMin, Double precioMax, Boolean enStock);

    ProductoResponse obtenerPorId(Long id);

    ProductoResponse crear(ProductoRequest request);

    ProductoResponse actualizar(Long id, ProductoRequest request);

    void eliminar(Long id);

    // Busca productos cuyo nombre contenga la cadena especificada.

    List<ProductoResponse> buscarPorNombre(String texto);

    // Obtiene una lista de productos ordenados por un campo específico.

    List<ProductoResponse> listarOrdenados(String campo, String orden);
}