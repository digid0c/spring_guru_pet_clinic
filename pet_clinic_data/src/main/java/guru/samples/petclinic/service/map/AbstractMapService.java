package guru.samples.petclinic.service.map;

import guru.samples.petclinic.model.BaseEntity;

import java.util.*;

import static java.util.Collections.max;

public abstract class AbstractMapService<T extends BaseEntity, ID extends Long> {

    protected Map<Long, T> map = new HashMap<>();

    Set<T> findAll() {
        return new HashSet<>(map.values());
    }

    T findById(ID id) {
        return map.get(id);
    }

    T save(T entity) {
        if (entity == null) {
            throw new RuntimeException("Entity cannot be null!");
        }

        if (entity.isNew()) {
            entity.setId(getNextId());
        }

        map.put(entity.getId(), entity);
        return map.get(entity.getId());
    }

    void deleteById(ID id) {
        map.remove(id);
    }

    void delete(T entity) {
        map.entrySet().removeIf(entry -> entry.getValue().equals(entity));
    }

    private Long getNextId() {
        return map.keySet().isEmpty() ? 1L : max(map.keySet()) + 1;
    }
}
