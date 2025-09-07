package com.example.repository;

import com.example.exception.InvalidEntityException;
import com.example.exception.InvalidFieldException;
import com.example.exception.InvalidPaginationException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.Entity;
import java.util.*;

/**
 * Builder Pattern class for creating dynamic queries.
 * Provides a developer experience similar to Spring Data JPA.
 *
 * Example usage:
 * repo.findBy(Employee.class)
 *     .where("firstName", "Ali")
 *     .like("lastName", "Y%")
 *     .orderBy("salary", true)   // ASC
 *     .page(1, 10)               // pagination
 *     .getResult();
 */
public class FindBuilder<T> {
    private final EntityManager em;
    private final Class<T> entityClass;

    // Equality conditions
    private final Map<String, Object> equalsConditions = new LinkedHashMap<>();
    // LIKE conditions
    private final Map<String, String> likeConditions = new LinkedHashMap<>();

    // Sorting
    private String orderField;
    private boolean ascending = true;

    // Pagination
    private int page = -1;
    private int pageSize = -1;

    public FindBuilder(EntityManager em, Class<T> entityClass) {
        this.em = em;
        this.entityClass = entityClass;
        validateEntityClass(entityClass);
    }

    /** Adds an equality condition (field = value). */
    public FindBuilder<T> where(String field, Object value) {
        if (field == null || field.trim().isEmpty()) {
            throw new InvalidFieldException("Field name cannot be null or empty.");
        }
        equalsConditions.put(field, value);
        return this;
    }

    /** Adds a LIKE condition (field like pattern). */
    public FindBuilder<T> like(String field, String pattern) {
        if (field == null || field.trim().isEmpty()) {
            throw new InvalidFieldException("Field name cannot be null or empty.");
        }
        if (pattern == null) {
            throw new InvalidFieldException("Pattern cannot be null.");
        }
        likeConditions.put(field, pattern);
        return this;
    }

    /** Adds an ORDER BY clause. */
    public FindBuilder<T> orderBy(String field, boolean ascending) {
        if (field == null || field.trim().isEmpty()) {
            throw new InvalidFieldException("Order field cannot be null or empty.");
        }
        this.orderField = field;
        this.ascending = ascending;
        return this;
    }

    /** Adds pagination info. */
    public FindBuilder<T> page(int page, int pageSize) {
        if (page <= 0 || pageSize <= 0) {
            throw new InvalidPaginationException("Page and page size must be greater than 0.");
        }
        this.page = page;
        this.pageSize = pageSize;
        return this;
    }

    /** Builds and executes the query, returning the result list. */
    public List<T> getResult() {
        String entityName = entityClass.getSimpleName();
        StringBuilder ql = new StringBuilder("from " + entityName + " e");

        String whereClause = buildWhereClause();
        if (!whereClause.isEmpty()) {
            ql.append(" where ").append(whereClause);
        }

        if (orderField != null) {
            ql.append(" order by lower(e.").append(orderField).append(")")
                    .append(ascending ? " asc" : " desc");
        }

        TypedQuery<T> query = em.createQuery(ql.toString(), entityClass);
        setParameters(query);

        if (page > 0 && pageSize > 0) {
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);
        }

        return query.getResultList();
    }

    /** Builds the WHERE clause from equals and like conditions. */
    private String buildWhereClause() {
        List<String> parts = new ArrayList<>();

        equalsConditions.keySet().forEach(field ->
                parts.add("e." + field + " = :" + field)
        );

        likeConditions.keySet().forEach(field ->
                parts.add("e." + field + " like :" + field)
        );

        return String.join(" and ", parts);
    }

    /** Sets parameters on the query. */
    private void setParameters(TypedQuery<T> query) {
        equalsConditions.forEach(query::setParameter);
        likeConditions.forEach(query::setParameter);
    }

    /** Validates that the provided class is a valid JPA entity. */
    private void validateEntityClass(Class<T> entityClass) {
        if (entityClass == null) {
            throw new InvalidEntityException("Entity class cannot be null.");
        }
        if (!entityClass.isAnnotationPresent(Entity.class)) {
            throw new InvalidEntityException(entityClass.getSimpleName() + " is not a JPA entity.");
        }
    }
}
