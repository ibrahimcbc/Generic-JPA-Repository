package com.example.repository;

import java.util.List;

public interface GenericRepository {
    <T> T findById(Class<T> entityClass, Object id);

    <T> List<T> findAll(Class<T> entityClass);

    <T> List<T> findBy(Class<T> entityClass, String fieldName, Object value);

    <T> T save(T entity);

    <T> void delete(T entity);

    <T> void deleteById(Class<T> entityClass, Object id);

    <T> long count(Class<T> entityClass);

    <T> boolean existsById(Class<T> entityClass, Object id);

    <T> T findOneBy(Class<T> entityClass, String fieldName, Object value);

    <T> List<T> findByLike(Class<T> entityClass, String fieldName, String pattern);

    <T> List<T> findAllSorted(Class<T> entityClass, String fieldName, boolean ascending);

}
