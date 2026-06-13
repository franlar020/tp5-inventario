package ar.edu.unlar.tp5_inventario.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    private Long id;
    private String nombre;
    private String descripcion;

}