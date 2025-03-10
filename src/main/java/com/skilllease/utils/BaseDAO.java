package com.skilllease.utils;

import java.util.List;
import java.util.Optional;

public interface BaseDAO<T, ID> {
    Optional<T> findById(ID id);

    List<T> findAll();

    T save(T entity);

    T update(T entity);

    void delete(ID id);
}
