package edu.course.lab06.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<ID, T extends Identifiable<ID>> {

    T save(T entity);

    Optional<T> findById(ID id);

    List<T> findAll(List<ID> ids);

    void delete(T entity);

    T update(T entity);
}
