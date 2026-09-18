package edu.course.lab06.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public class LinkedHashMapRepository<ID, T extends Identifiable<ID>> implements Repository<ID, T> {

    private final Map<ID, T> map = new LinkedHashMap<>();

    @Override
    public T save(T entity) {
        ID id = requireId(entity);
        if (map.containsKey(id)) {
            throw new IllegalStateException("entity with id " + id + " already exists");
        }
        map.put(id, entity);
        return entity;
    }

    @Override
    public Optional<T> findById(ID id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        return Optional.ofNullable(map.get(id));
    }

    @Override
    public List<T> findAll(List<ID> ids) {
        if (ids == null) {
            throw new IllegalArgumentException("ids cannot be null");
        }
        List<T> found = new ArrayList<>();
        for (ID id : ids) {
            T entity = map.get(id);
            if (entity != null) {
                found.add(entity);
            }
        }
        return found;
    }

    @Override
    public void delete(T entity) {
        map.remove(requireId(entity));
    }

    @Override
    public T update(T entity) {
        ID id = requireId(entity);
        if (!map.containsKey(id)) {
            throw new NoSuchElementException("entity with id " + id + " not found");
        }
        map.put(id, entity);
        return entity;
    }

    public List<T> findAll() {
        return new ArrayList<>(map.values());
    }

    public int size() {
        return map.size();
    }

    private ID requireId(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity cannot be null");
        }
        ID id = entity.getId();
        if (id == null) {
            throw new IllegalArgumentException("entity id cannot be null");
        }
        return id;
    }
}
