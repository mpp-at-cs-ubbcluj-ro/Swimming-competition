package org.example.repository;

import org.example.model.Entity;

import java.util.Collection;

public interface IRepository<T extends Entity<Tid>, Tid> {
    void add(T elem);
    void delete(T elem);
    void update(T elem, Tid id);
    T findById(Tid id);
    Iterable<T> findAll();
}
