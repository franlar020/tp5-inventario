package ar.edu.unlar.tp5_inventario.service;

import ar.edu.unlar.tp5_inventario.dto.MetricaPerformance;
import ar.edu.unlar.tp5_inventario.model.Categoria;
import ar.edu.unlar.tp5_inventario.model.Producto;
import ar.edu.unlar.tp5_inventario.repository.InMemoryProductoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PerformanceReportService {

    /**
     * Genera el reporte de performance ejecutando pruebas con 1k, 10k y 100k registros.
     *
     * @return lista de métricas formateadas para exponer en JSON
     * @since 1.0
     */
    public List<MetricaPerformance> generarReporte() {
        List<MetricaPerformance> reporte = new ArrayList<>();
        int[] volumenes = {1000, 10000, 100000};

        // Preparamos los mapas para guardar los tiempos de cada operación
        Map<String, Long> tiemposFindAll = new LinkedHashMap<>();
        Map<String, Long> tiemposFindById = new LinkedHashMap<>();
        Map<String, Long> tiemposSave = new LinkedHashMap<>();
        Map<String, Long> tiemposBuscar = new LinkedHashMap<>();

        for (int volumen : volumenes) {
            // Instanciamos un repo limpio solo para esta iteración de prueba
            InMemoryProductoRepository repoPrueba = new InMemoryProductoRepository();
            poblarRepositorio(repoPrueba, volumen);

            // 1. Medir GET /api/productos (O(n))
            long inicio = System.nanoTime();
            repoPrueba.findAll();
            tiemposFindAll.put(volumen + " registros", System.nanoTime() - inicio);

            // 2. Medir GET /api/productos/{id} (O(1))
            // Buscamos un ID que sabemos que está en el medio
            Long idABuscar = (long) (volumen / 2);
            inicio = System.nanoTime();
            repoPrueba.findById(idABuscar);
            tiemposFindById.put(volumen + " registros", System.nanoTime() - inicio);

            // 3. Medir POST /api/productos (O(1))
            Producto nuevo = new Producto(null, "Test Insert", "Desc", 100.0, 10, new Categoria(1L, "Cat", "Desc"));
            inicio = System.nanoTime();
            repoPrueba.save(nuevo);
            tiemposSave.put(volumen + " registros", System.nanoTime() - inicio);

            // 4. Medir GET /api/productos/buscar?q= (O(n*m))
            inicio = System.nanoTime();
            repoPrueba.buscarPorNombre("Producto"); // Buscará una palabra muy común en la prueba
            tiemposBuscar.put(volumen + " registros", System.nanoTime() - inicio);
            
            // Sugerencia para limpieza de GC (opcional, ayuda a que las pruebas no hagan tanto ruido)
            System.gc();
        }

        // Armamos el reporte final
        reporte.add(new MetricaPerformance("GET /api/productos", "O(n)", tiemposFindAll));
        reporte.add(new MetricaPerformance("GET /api/productos/{id}", "O(1) amortizado (ConcurrentHashMap.get)", tiemposFindById));
        reporte.add(new MetricaPerformance("POST /api/productos", "O(1) amortizado (ConcurrentHashMap.put)", tiemposSave));
        reporte.add(new MetricaPerformance("GET /api/productos/buscar?q=", "O(n * m) iteración + contains()", tiemposBuscar));

        return reporte;
    }

    /**
     * Carga datos ficticios en el repositorio temporal.
     */
    private void poblarRepositorio(InMemoryProductoRepository repo, int cantidad) {
        Categoria catDump = new Categoria(1L, "Dummy Category", "Dummy Description");
        for (int i = 1; i <= cantidad; i++) {
            Producto p = new Producto(null, "Producto " + i, "Desc " + i, 10.5 * i, 50, catDump);
            repo.save(p);
        }
    }
}

