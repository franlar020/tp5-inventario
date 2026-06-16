package ar.edu.unlar.tp5_inventario.service;

import ar.edu.unlar.tp5_inventario.dto.CategoriaRequest;
import ar.edu.unlar.tp5_inventario.dto.CategoriaResponse;
import java.util.List;

public interface CategoriaService {

    List<CategoriaResponse> obtenerTodas();
    CategoriaResponse obtenerPorId(Long id);
    CategoriaResponse crear(CategoriaRequest request);
    CategoriaResponse actualizar(Long id, CategoriaRequest request);
    void eliminar(Long id);
}