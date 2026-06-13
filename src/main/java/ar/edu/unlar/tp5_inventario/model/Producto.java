package ar.edu.unlar.tp5_inventario.model;


import lombok.Getter;
import lombok.Setter;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
public class Producto {

    private Long id;
    private String nombre;
    private String descripcion;
    private double precio;
    private Categoria categoria;

    /**
     * Variable de estado interno para la cantidad disponible.
     * Se excluye explícitamente de Lombok para manejar los retornos atómicos manualmente.
     */

    @Getter(lombok.AccessLevel.NONE)
    @Setter(lombok.AccessLevel.NONE)
    private final AtomicInteger stock;

    // Construye un nuevo Producto estableciendo su estado inicial
    public Producto(Long id, String nombre, String descripcion, double precio, int stockInicial, Categoria categoria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        // Se asegura el requerimiento de negocio: stock inicial >= 0
        this.stock = new AtomicInteger(Math.max(0, stockInicial)); 
    }

    // Obtiene la cantidad actual en stock
    public int getStock() {
        return stock.get();
    }

    // Incrementa el stock actual de forma atomica.
    public int incrementarStock(int cantidad) {
        return stock.addAndGet(cantidad);
    }


    // Decrementa el stock actual de forma atomica
    public int decrementarStock(int cantidad) {
        return stock.addAndGet(-cantidad);
    }
}