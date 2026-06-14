package ar.edu.unlar.tp5_inventario.exception;

// Excepción lanzada cuando se intenta acceder o manipular un recurso que no existe.

public class ResourceNotFoundException extends RuntimeException {

    // Construye una nueva ResourceNotFoundException con el mensaje especificado.

    public ResourceNotFoundException(String message) {
        super(message);
    }
}