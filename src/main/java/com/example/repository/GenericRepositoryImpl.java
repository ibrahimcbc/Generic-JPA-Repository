package com.example.repository;

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
}
