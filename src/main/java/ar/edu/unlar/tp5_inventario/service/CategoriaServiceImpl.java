package ar.edu.unlar.tp5_inventario.service;

import ar.edu.unlar.tp5_inventario.dto.CategoriaRequest;
import ar.edu.unlar.tp5_inventario.dto.CategoriaResponse;
import ar.edu.unlar.tp5_inventario.exception.BusinessRuleException;
import ar.edu.unlar.tp5_inventario.exception.ResourceNotFoundException;
import ar.edu.unlar.tp5_inventario.model.Categoria;
import ar.edu.unlar.tp5_inventario.model.Producto;
import ar.edu.unlar.tp5_inventario.repository.CategoriaRepository;
import ar.edu.unlar.tp5_inventario.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

//Implementación del servicio de gestión de categorías.


@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;

    // Inyección de dependencias por constructor (Cumple principio DIP y sin @Autowired)
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    public List<CategoriaResponse> obtenerTodas() {
        return categoriaRepository.findAll().stream()
                .map(this::mapearAResponse)
                .toList();
    }

    @Override
    public CategoriaResponse obtenerPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la categoría con ID: " + id));
        return mapearAResponse(categoria);
    }

    @Override
    public CategoriaResponse crear(CategoriaRequest request) {
        Categoria categoria = new Categoria();
        categoria.setNombre(request.nombre());
        categoria.setDescripcion(request.descripcion());
        
        Categoria guardada = categoriaRepository.save(categoria);
        return mapearAResponse(guardada);
    }

    @Override
    public CategoriaResponse actualizar(Long id, CategoriaRequest request) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la categoría con ID: " + id));
        
        categoria.setNombre(request.nombre());
        categoria.setDescripcion(request.descripcion());
        
        Categoria actualizada = categoriaRepository.save(categoria);
        return mapearAResponse(actualizada);
    }

    @Override
    public void eliminar(Long id) {
        // Regla de negocio: Validar si la categoría existe
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("No se encontró la categoría con ID: " + id);
        }

        // Regla de negocio: Verificar que no haya productos usando esta categoría (Error 409)
        List<Producto> productosAsociados = productoRepository.findByCategoria(id);
        if (!productosAsociados.isEmpty()) {
            throw new BusinessRuleException("No se puede eliminar la categoría porque tiene " 
                    + productosAsociados.size() + " producto(s) asociado(s).");
        }

        categoriaRepository.deleteById(id);
    }

    // Mapea una entidad Categoria a su DTO de respuesta.

    private CategoriaResponse mapearAResponse(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion()
        );
    }
}