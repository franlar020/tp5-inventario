package ar.edu.unlar.tp5_inventario.controller;

import ar.edu.unlar.tp5_inventario.dto.ProductoRequest;
import ar.edu.unlar.tp5_inventario.dto.ProductoResponse;
import ar.edu.unlar.tp5_inventario.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listarTodos(
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            @RequestParam(required = false) Boolean enStock) {
        return ResponseEntity.ok(productoService.obtenerTodos(categoriaId, precioMin, precioMax, enStock));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        ProductoResponse response = productoService.crear(request);
        return ResponseEntity
                .created(URI.create("/api/productos/" + response.id()))
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(productoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoResponse>> buscar(@RequestParam String q) {
        if (q == null || q.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(productoService.buscarPorNombre(q));
    }

    @GetMapping("/ordenados")
    public ResponseEntity<List<ProductoResponse>> listarOrdenados(
            @RequestParam(defaultValue = "nombre") String campo,
            @RequestParam(defaultValue = "asc") String orden) {
        return ResponseEntity.ok(productoService.listarOrdenados(campo, orden));
    }
}