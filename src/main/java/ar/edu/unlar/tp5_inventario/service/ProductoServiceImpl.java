package ar.edu.unlar.tp5_inventario.service;

import ar.edu.unlar.tp5_inventario.dto.CategoriaResponse;
import ar.edu.unlar.tp5_inventario.dto.ProductoRequest;
import ar.edu.unlar.tp5_inventario.dto.ProductoResponse;
import ar.edu.unlar.tp5_inventario.exception.ResourceNotFoundException;
import ar.edu.unlar.tp5_inventario.model.Categoria;
import ar.edu.unlar.tp5_inventario.model.Producto;
import ar.edu.unlar.tp5_inventario.repository.CategoriaRepository;
import ar.edu.unlar.tp5_inventario.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

// Implementación del servicio de gestión de productos.
 
@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public List<ProductoResponse> obtenerTodos(Long categoriaId, Double precioMin, Double precioMax, Boolean enStock) {
        Stream<Producto> stream = productoRepository.findAll().stream();

        if (categoriaId != null) {
            stream = stream.filter(p -> p.getCategoria().getId().equals(categoriaId));
        }
        if (precioMin != null) {
            stream = stream.filter(p -> p.getPrecio() >= precioMin);
        }
        if (precioMax != null) {
            stream = stream.filter(p -> p.getPrecio() <= precioMax);
        }
        if (enStock != null && enStock) {
            stream = stream.filter(p -> p.getStock() > 0);
        }

        return stream.map(this::mapearAResponse).toList();
    }

    @Override
    public ProductoResponse obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto con ID: " + id));
        return mapearAResponse(producto);
    }

    @Override
    public ProductoResponse crear(ProductoRequest request) {
        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la categoría con ID: " + request.categoriaId()));

        Producto producto = new Producto(
                null, 
                request.nombre(), 
                request.descripcion(), 
                request.precio(), 
                request.stockInicial(), 
                categoria
        );

        Producto guardado = productoRepository.save(producto);
        return mapearAResponse(guardado);
    }

    @Override
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el producto con ID: " + id));

        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la categoría con ID: " + request.categoriaId()));

        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setCategoria(categoria);
        // Nota: El stock NO se actualiza aquí por requerimiento de negocio. Se hace por Movimiento.

        Producto actualizado = productoRepository.save(producto);
        return mapearAResponse(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public List<ProductoResponse> buscarPorNombre(String texto) {
        return productoRepository.buscarPorNombre(texto).stream()
                .map(this::mapearAResponse)
                .toList();
    }

    @Override
    public List<ProductoResponse> listarOrdenados(String campo, String orden) {
        Comparator<Producto> comparador;

        switch (campo.toLowerCase()) {
            case "precio": comparador = Comparator.comparing(Producto::getPrecio); break;
            case "stock": comparador = Comparator.comparing(Producto::getStock); break;
            case "nombre": default: comparador = Comparator.comparing(Producto::getNombre); break;
        }

        if ("desc".equalsIgnoreCase(orden)) {
            comparador = comparador.reversed();
        }

        return productoRepository.findAll().stream()
                .sorted(comparador) // Complejidad O(n log n)
                .map(this::mapearAResponse)
                .toList();
    }

    // Mapea un Producto a ProductoResponse, incluyendo el DTO anidado de Categoria.
     
    private ProductoResponse mapearAResponse(Producto producto) {
        CategoriaResponse catResponse = new CategoriaResponse(
                producto.getCategoria().getId(),
                producto.getCategoria().getNombre(),
                producto.getCategoria().getDescripcion()
        );

        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                catResponse
        );
    }
}