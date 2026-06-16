package ar.edu.unlar.tp5_inventario.service;

import ar.edu.unlar.tp5_inventario.model.NivelAlerta;

// Interfaz para el patrón Strategy aplicado a las reglas de evaluación de stock.

public interface ReglaAlertaStrategy {

    // Evalúa el nivel de alerta correspondiente según un nivel de stock.

    NivelAlerta evaluar(int stock);
}