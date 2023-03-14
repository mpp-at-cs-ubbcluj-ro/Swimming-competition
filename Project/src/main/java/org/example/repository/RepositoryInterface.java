package org.example.repository;

import java.util.Collection;

public interface RepositoryInterface<T, Tid> {
    void add(T elem);
    void delete(T elem);
    void update(T elem, Tid id);
    T findById(Tid id);
    Iterable<T> findAll();
    Collection<T> getAll();
}