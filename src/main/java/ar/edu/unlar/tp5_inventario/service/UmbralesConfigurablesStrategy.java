package ar.edu.unlar.tp5_inventario.service;

import ar.edu.unlar.tp5_inventario.config.StockConfig;
import ar.edu.unlar.tp5_inventario.model.NivelAlerta;
import org.springframework.stereotype.Component;

// Implementación de ReglaAlertaStrategy basada en los umbrales definidos en application.properties.

@Component
public class UmbralesConfigurablesStrategy implements ReglaAlertaStrategy {

    private final StockConfig config;

    public UmbralesConfigurablesStrategy(StockConfig config) {
        this.config = config;
    }

    @Override
    public NivelAlerta evaluar(int stock) {
        if (stock < config.critico()) {
            return NivelAlerta.CRITICO;
        } else if (stock < config.minimo()) {
            return NivelAlerta.BAJO;
        } else {
            return NivelAlerta.NORMAL;
        }
    }
}