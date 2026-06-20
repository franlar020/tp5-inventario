package ar.edu.unlar.tp5_inventario.config;

import ar.edu.unlar.tp5_inventario.dto.CategoriaRequest;
import ar.edu.unlar.tp5_inventario.dto.ProductoRequest;
import ar.edu.unlar.tp5_inventario.service.CategoriaService;
import ar.edu.unlar.tp5_inventario.service.ProductoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Inicializador de datos predeterminados al arranque de la aplicación.
 * Útil para pruebas manuales y verificación de Swagger sin base vacía.
 *
 * @author Grupo 3 — Inventario Inteligente
 * @since 1.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoriaService categoriaService;
    private final ProductoService productoService;

    public DataInitializer(CategoriaService categoriaService, ProductoService productoService) {
        this.categoriaService = categoriaService;
        this.productoService = productoService;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Cargamos categorías iniciales
        var catElectronica = categoriaService.crear(new CategoriaRequest("Electrónica", "Dispositivos y gadgets"));
        var catLimpieza = categoriaService.crear(new CategoriaRequest("Limpieza", "Productos de higiene y hogar"));

        // 2. Cargamos algunos productos de prueba
        productoService.crear(new ProductoRequest(
                "Notebook Dell XPS 15",
                "Laptop de alto rendimiento para desarrolladores",
                1599.99,
                25, // Stock NORMAL (Asumiendo critico=3, minimo=10)
                catElectronica.id()
        ));

        productoService.crear(new ProductoRequest(
                "Auriculares Redragon Zeus Pro",
                "Auriculares inalámbricos gaming",
                85.50,
                5, // Stock BAJO (menor a 10, mayor a 3)
                catElectronica.id()
        ));

        productoService.crear(new ProductoRequest(
                "Desengrasante Industrial 5L",
                "Para limpieza profunda",
                12.00,
                2, // Stock CRITICO (menor a 3)
                catLimpieza.id()
        ));
        
        System.out.println("✅ [INVENTARIO INTELIGENTE] Datos iniciales cargados con éxito.");
    }
}