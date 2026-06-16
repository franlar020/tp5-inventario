package ar.edu.unlar.tp5_inventario.service;

import ar.edu.unlar.tp5_inventario.dto.AlertaStockResponse;
import java.util.List;

// Contrato para el servicio de monitoreo de alertas de stock.
 
public interface AlertaService {

    List<AlertaStockResponse> obtenerProductosConStockBajo();
}