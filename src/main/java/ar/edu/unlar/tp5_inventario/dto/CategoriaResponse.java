package ar.edu.unlar.tp5_inventario.dto;


 // DTO para la exposición de datos de una Categoría hacia el cliente HTTP.

public record CategoriaResponse(
        Long id,
        String nombre,
        String descripcion
) {}