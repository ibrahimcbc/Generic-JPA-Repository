package com.example.repository;

import com.example.exception.*;

import javax.persistence.EntityManager;
import java.util.List;

public class GenericRepositoryImpl implements GenericRepository {

    private final EntityManager em;

    public GenericRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public <T> T findById(Class<T> entityClass, Object id) {
        return em.find(entityClass, id);
    }

    @Override
    public <T> List<T> findAll(Class<T> entityClass) {
        String entityName = entityClass.getSimpleName();
        return em.createQuery("from " + entityName, entityClass).getResultList();
    }

    @Override
    public <T> List<T> findBy(Class<T> entityClass, String fieldName, Object value) {
        String entityName = entityClass.getSimpleName();
        String ql = "from " + entityName + " e where e." + fieldName + " = :val";
        return em.createQuery(ql, entityClass)
                .setParameter("val", value)
                .getResultList();
    }

    @Override
    public <T> T save(T entity) {
        return em.merge(entity);
    }

    @Override
    public <T> void delete(T entity) {
        T managed = em.contains(entity) ? entity : em.merge(entity);
        em.remove(managed);
    }

    @Override
    public <T> void deleteById(Class<T> entityClass, Object id) {
        T entity = em.find(entityClass, id);
        if (entity != null) {
            em.remove(entity);
        }
    }

    @Override
    public <T> long count(Class<T> entityClass) {
        validateEntityClass(entityClass);
        String entityName = entityClass.getSimpleName();
        return em.createQuery("select count(e) from " + entityName + " e", Long.class)
                .getSingleResult();
    }

    @Override
    public <T> boolean existsById(Class<T> entityClass, Object id) {
        validateEntityClass(entityClass);
        if (id == null) {
            throw new InvalidIdException("ID null olamaz: " + entityClass.getSimpleName());
        }
        return em.find(entityClass, id) != null;
    }

    private <T> void validateEntityClass(Class<T> entityClass) {
        if (entityClass == null) {
            throw new InvalidEntityException("Entity sınıfı null olamaz.");
        }
        if (!entityClass.isAnnotationPresent(javax.persistence.Entity.class)) {
            throw new InvalidEntityException(entityClass.getSimpleName() + " bir JPA entity değildir.");
        }
    }
}
