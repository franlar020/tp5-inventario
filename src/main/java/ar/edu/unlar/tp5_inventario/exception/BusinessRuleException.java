package ar.edu.unlar.tp5_inventario.exception;

/**
 * Excepción lanzada cuando se viola una regla de negocio del sistema
 * (ej. eliminar una categoría con productos asociados).
 * Resultará en un estado HTTP 409 (Conflict).
 */

public class BusinessRuleException extends RuntimeException {
    
    //Construye una nueva BusinessRuleException con el mensaje especificado.
     
    public BusinessRuleException(String message) {
        super(message);
    }
}