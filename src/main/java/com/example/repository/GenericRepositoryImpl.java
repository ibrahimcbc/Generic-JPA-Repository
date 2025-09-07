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
        validateEntityClass(entityClass);
        if (id == null) {
            throw new InvalidIdException("ID null olamaz: " + entityClass.getSimpleName());
        }
        T entity = em.find(entityClass, id);
        if (entity == null) {
            throw new EntityNotFoundException(entityClass.getSimpleName() + " için ID bulunamadı: " + id);
        }
        return entity;
    }

    @Override
    public <T> List<T> findAll(Class<T> entityClass) {
        validateEntityClass(entityClass);
        String entityName = entityClass.getSimpleName();
        return em.createQuery("from " + entityName, entityClass).getResultList();
    }

    @Override
    public <T> List<T> findBy(Class<T> entityClass, String fieldName, Object value) {
        validateEntityClass(entityClass);
        if (fieldName == null || fieldName.trim().isEmpty()) {
            throw new InvalidFieldException("Alan adı boş olamaz.");
        }
        String entityName = entityClass.getSimpleName();
        String ql = "from " + entityName + " e where e." + fieldName + " = :val";
        return em.createQuery(ql, entityClass)
                .setParameter("val", value)
                .getResultList();
    }

    @Override
    public <T> T save(T entity) {
        if (entity == null) {
            throw new InvalidEntityException("Kaydedilecek entity null olamaz.");
        }
        Class<?> entityClass = entity.getClass();
        if (!entityClass.isAnnotationPresent(javax.persistence.Entity.class)) {
            throw new InvalidEntityException(entityClass.getSimpleName() + " bir JPA entity değildir.");
        }
        return em.merge(entity);
    }

    @Override
    public <T> void delete(T entity) {
        if (entity == null) {
            throw new InvalidEntityException("Silinecek entity null olamaz.");
        }
        Class<?> entityClass = entity.getClass();
        if (!entityClass.isAnnotationPresent(javax.persistence.Entity.class)) {
            throw new InvalidEntityException(entityClass.getSimpleName() + " bir JPA entity değildir.");
        }
        T managed = em.contains(entity) ? entity : em.merge(entity);
        if (managed == null) {
            throw new EntityNotFoundException("Entity silinemedi: " + entityClass.getSimpleName());
        }
        em.remove(managed);
    }

    @Override
    public <T> void deleteById(Class<T> entityClass, Object id) {
        validateEntityClass(entityClass);
        if (id == null) {
            throw new InvalidIdException("ID null olamaz: " + entityClass.getSimpleName());
        }
        T entity = em.find(entityClass, id);
        if (entity == null) {
            throw new EntityNotFoundException(entityClass.getSimpleName() + " için ID bulunamadı: " + id);
        }
        em.remove(entity);
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

    @Override
    public <T> T findOneBy(Class<T> entityClass, String fieldName, Object value) {
        validateEntityClass(entityClass);
        if (fieldName == null || fieldName.trim().isEmpty()) {
            throw new InvalidFieldException("Alan adı boş olamaz.");
        }
        String entityName = entityClass.getSimpleName();
        String ql = "from " + entityName + " e where e." + fieldName + " = :val";
        List<T> results = em.createQuery(ql, entityClass)
                .setParameter("val", value)
                .getResultList();
        if (results.isEmpty()) {
            throw new EntityNotFoundException(entityClass.getSimpleName() + " için " + fieldName + " bulunamadı: " + value);
        }
        if (results.size() > 1) {
            throw new NonUniqueResultException(entityClass.getSimpleName() + " için birden fazla sonuç bulundu (field=" + fieldName + ")");
        }
        return results.get(0);
    }

    @Override
    public <T> List<T> findByLike(Class<T> entityClass, String fieldName, String pattern) {
        validateEntityClass(entityClass);
        if (fieldName == null || fieldName.trim().isEmpty()) {
            throw new InvalidFieldException("Alan adı boş olamaz.");
        }
        if (pattern == null) {
            throw new InvalidFieldException("Pattern null olamaz.");
        }
        String entityName = entityClass.getSimpleName();
        String ql = "from " + entityName + " e where e." + fieldName + " like :pattern";
        return em.createQuery(ql, entityClass)
                .setParameter("pattern", pattern)
                .getResultList();
    }

    @Override
    public <T> List<T> findAllSorted(Class<T> entityClass, String sortField, boolean ascending) {
        validateEntityClass(entityClass);
        if (sortField == null || sortField.trim().isEmpty()) {
            throw new InvalidFieldException("Sıralama alanı boş olamaz.");
        }
        String entityName = entityClass.getSimpleName();
        String order = ascending ? "asc" : "desc";
        String ql = "from " + entityName + " e order by lower(e." + sortField + ") " + order;
        return em.createQuery(ql, entityClass).getResultList();
    }

    @Override
    public <T> FindBuilder<T> findBy(Class<T> entityClass) {
        validateEntityClass(entityClass);
        return new FindBuilder<>(em, entityClass);
    }

    @Override
    public <T> List<T> findAllPaged(Class<T> entityClass, int page, int pageSize) {
        validateEntityClass(entityClass);
        if (page <= 0 || pageSize <= 0) {
            throw new InvalidPaginationException("Sayfa ve sayfa boyutu 0 veya negatif olamaz.");
        }
        String entityName = entityClass.getSimpleName();
        return em.createQuery("from " + entityName, entityClass)
                .setFirstResult((page - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
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
