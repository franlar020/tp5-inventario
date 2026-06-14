package ar.edu.unlar.tp5_inventario.repository;

import java.util.List;
import java.util.Optional;

// Interfaz genérica para repositorios que define las operaciones CRUD basicas
public interface IGenericRepository<T, ID> {

    List<T> findAll();

    Optional<T> findById(ID id);

    T save(T entity);

    void deleteById(ID id);

    boolean existsById(ID id);

    long count();
}