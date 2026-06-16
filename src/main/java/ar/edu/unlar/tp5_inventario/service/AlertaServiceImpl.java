package ar.edu.unlar.tp5_inventario.service;

import ar.edu.unlar.tp5_inventario.dto.AlertaStockResponse;
import ar.edu.unlar.tp5_inventario.model.NivelAlerta;
import ar.edu.unlar.tp5_inventario.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// Implementación del servicio de alertas inyectando una estrategia dinámica de evaluación.*

@Service
public class AlertaServiceImpl implements AlertaService {

    private final ProductoRepository productoRepository;
    private final ReglaAlertaStrategy alertaStrategy;

    public AlertaServiceImpl(ProductoRepository productoRepository, ReglaAlertaStrategy alertaStrategy) {
        this.productoRepository = productoRepository;
        this.alertaStrategy = alertaStrategy;
    }

    @Override
    public List<AlertaStockResponse> obtenerProductosConStockBajo() {
        return productoRepository.findAll().stream()
                .map(p -> {
                    NivelAlerta nivel = alertaStrategy.evaluar(p.getStock());
                    return new AlertaStockResponse(p.getId(), p.getNombre(), p.getStock(), nivel);
                })
                // Filtramos y nos quedamos solo con los que no están NORMAL
                .filter(a -> a.nivelAlerta() != NivelAlerta.NORMAL)
                .toList();
    }
}