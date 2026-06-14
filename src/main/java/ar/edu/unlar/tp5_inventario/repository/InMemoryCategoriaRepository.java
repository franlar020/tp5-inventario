package ar.edu.unlar.tp5_inventario.repository;

import ar.edu.unlar.tp5_inventario.model.Categoria;
import org.springframework.stereotype.Repository;

//Implementación en memoria del repositorio de Categorías.

@Repository
public class InMemoryCategoriaRepository extends GenericInMemoryRepository<Categoria, Long> implements CategoriaRepository {
}