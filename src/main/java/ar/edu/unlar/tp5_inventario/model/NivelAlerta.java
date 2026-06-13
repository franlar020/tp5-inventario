package ar.edu.unlar.tp5_inventario.model;

public enum NivelAlerta {
    /** El stock es mayor o igual al minimo configurado. */
    NORMAL,    
    
    /** El stock está por debajo del minimo, pero es mayor o igual al critico. */
    BAJO,      
    
    /** El stock es inferior al umbral critico configurado. */
    CRITICO    
}