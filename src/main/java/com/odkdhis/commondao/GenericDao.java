package com.odkdhis.commondao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Generic Repository, providing basic CRUD operations
 *
 * @author ssvmoh
 *
 * @param <T>  the entity type
 * @param <ID> the primary key type
 */
public interface GenericDao<T, ID extends Serializable> {

    /**
     * Get the Class of the entity.
     *
     * @return the class
     */
    Class<T> getEntityClass();

    /**
     * Find an entity by its primary key
     *
     * @param id the primary key
     * @return the entity
     */
    T findBeanById(final ID id);

    /**
     * Get reference of the entity whose state may be lazily fetched.
     *
     * @param id
     * @return
     */
    T getReference(final ID id);

    Integer findDuplicate(final ID id, String paramFieldName, String paramFieldValue);

    /**
     * Find using a named query.
     *
     * @param queryName the name of the query
     * @param params    the query parameters
     *
     * @return the list of entities
     */
    List<T> findByNamedQuery(final String queryName, Object... params);

    /**
     * Find using a named query.
     *
     * @param queryName the name of the query
     * @param params    the query parameters
     *
     * @return the list of entities
     */
    List<T> findByNamedQuery(final String queryName, final Map<String, ? extends Object> params);

    /**
     * save an entity. This can be either a INSERT or UPDATE in the database.
     *
     * @param entity the entity to save
     *
     * @return the saved entity
     */
    void saveBean(final T entity) throws Exception;

    /**
     * delete an entity from the database.
     *
     * @param entity the entity to delete
     */
    void deleteBean(final T entity) throws Exception;

    /**
     * delete an entity by its primary key
     *
     * @param id the primary key of the entity to delete
     */
    void deleteBeanById(final ID id) throws Exception;

    /**
     * delete batch entities by their primary keys array
     *
     * @param ids [] the primary key of entities to delete
     */
    void deleteBatchById(ID ids[]) throws Exception;

    /**
     * delete batch entities by their primary keys array
     *
     * @param ids [] the primary key of entities to delete
     */
    void deleteBeans(ID ids[]) throws Exception;

    public List<T> findByCriteria(final int firstResult, final int maxResults, final CriteriaQuery<T> select);

    public List<T> findByCriteria2(final int firstResult, final int maxResults, final List<Predicate> predicates);

    CriteriaBuilder createCriteriaBuilder();

    CriteriaQuery<T>  createCriteriaQuery();

    Root<T> createCriteriaRoot();

    public List<T> findAll(final int firstResult, final int maxResults);

    public List<T> list(final int firstResult, final int maxResults);

    /**
     * Find using a named query.
     *
     * @param queryName the name of the query
     * @param params    the query parameters
     *
     * @return the list of entities
     */
    List<T> findByNamedQueryAndNamedParams(final String queryName, final Map<String, ? extends Object> params);

    public String wrapToLike(String value);

}
