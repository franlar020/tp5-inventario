package ar.edu.unlar.tp5_inventario.dto;
import ar.edu.unlar.tp5_inventario.model.NivelAlerta;

//DTO exclusivo para el reporte de productos con problemas de stock.

public record AlertaStockResponse(
        Long id,
        String nombre,
        int stockActual,
        NivelAlerta nivelAlerta
) {}