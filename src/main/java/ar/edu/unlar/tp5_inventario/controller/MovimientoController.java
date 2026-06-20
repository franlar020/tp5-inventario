package ar.edu.unlar.tp5_inventario.controller;

import ar.edu.unlar.tp5_inventario.dto.MovimientoRequest;
import ar.edu.unlar.tp5_inventario.dto.MovimientoResponse;
import ar.edu.unlar.tp5_inventario.service.MovimientoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @PostMapping
    public ResponseEntity<MovimientoResponse> registrar(@Valid @RequestBody MovimientoRequest request) {
        MovimientoResponse response = movimientoService.registrarMovimiento(request);
        return ResponseEntity
                .created(URI.create("/api/movimientos/" + response.id()))
                .body(response);
    }

    @GetMapping("/producto/{id}")
    public ResponseEntity<List<MovimientoResponse>> obtenerHistorial(@PathVariable Long id) {
        return ResponseEntity.ok(movimientoService.obtenerHistorialPorProducto(id));
    }
}
