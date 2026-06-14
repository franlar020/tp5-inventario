package ar.edu.unlar.tp5_inventario.exception;

/**
Excepción lanzada cuando se intenta registrar una salida de inventario
por una cantidad mayor al stock disponible. Resultará en un HTTP 409 (Conflict).*/

public class InsufficientStockException extends RuntimeException {

    // Construye una nueva InsufficientStockException con el mensaje especificado.

    public InsufficientStockException(String message) {
        super(message);
    }
}