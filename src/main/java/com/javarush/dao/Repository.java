package com.javarush;

import java.util.List;

public interface Repository<T> {
    void save(T t);
    void update(T t);
    void delete(T t);
    T findById(Long id);
    List<T> findAll();
}
