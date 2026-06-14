package ar.edu.unlar.tp5_inventario.repository;
import ar.edu.unlar.tp5_inventario.exception.ResourceNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public abstract class GenericInMemoryRepository<T, ID> implements IGenericRepository<T, ID> {

    protected final ConcurrentHashMap<ID, T> dataStore = new ConcurrentHashMap<>();
    protected final AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public List<T> findAll() {
        return new ArrayList<>(dataStore.values());
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(dataStore.get(id));
    }

    @Override
    @SuppressWarnings("unchecked")
    public T save(T entity) {
        try {
            Method getIdMethod = entity.getClass().getMethod("getId");
            ID id = (ID) getIdMethod.invoke(entity);

            if (id == null) {
                // Generamos un nuevo ID atómicamente si no existe
                Long newId = idGenerator.incrementAndGet();
                Method setIdMethod = entity.getClass().getMethod("setId", Long.class);
                setIdMethod.invoke(entity, newId);
                id = (ID) newId;
            }

            dataStore.put(id, entity);
            return entity;

        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la entidad mediante reflexión", e);
        }
    }

    @Override
    public void deleteById(ID id) {
        if (dataStore.remove(id) == null) {
            throw new ResourceNotFoundException("No se encontró el recurso con ID: " + id);
        }
    }

    @Override
    public boolean existsById(ID id) {
        return dataStore.containsKey(id);
    }

    @Override
    public long count() {
        return dataStore.size();
    }
}