package ar.edu.unlar.tp5_inventario.dto;

// DTO para la exposición de datos de un Producto hacia el cliente HTTP.


public record ProductoResponse(
        Long id,
        String nombre,
        String descripcion,
        double precio,
        int stock,
        CategoriaResponse categoria
) {}