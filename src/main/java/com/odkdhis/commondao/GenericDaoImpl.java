package com.odkdhis.commondao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.hibernate.Session;

import org.slf4j.LoggerFactory;

/**
 * JPA implementation of the GenericRepository.
 *
 * @author ssvmoh
 *
 * @param <T> The persistent type
 * @param <ID> The primary key type
 */
@PersistenceContext(name = "primary")

public class GenericDaoImpl<T, ID extends Serializable> implements GenericDao<T, ID> {
    
    protected static DecimalFormat formatter = new DecimalFormat("#,##0.0");
    protected static DecimalFormat formatter2 = new DecimalFormat("#,##0");

    private final Class<T> persistentClass;

    protected org.slf4j.Logger log = LoggerFactory.getLogger(getClass());

    @PersistenceContext
    protected EntityManager em;

    protected Session getSession() {
        Session session = em.unwrap(Session.class);
        return session;
    }

    @SuppressWarnings("unchecked")
    public GenericDaoImpl() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public GenericDaoImpl(final Class<T> persistentClass) {
        super();
        this.persistentClass = persistentClass;
    }

    /**
     * @see GenericDao#getEntityClass()
     */
    public Class<T> getEntityClass() {
        return persistentClass;
    }

    /**
     * @see GenericDao#save(Object) #save(java.lang.Object)
     */
    public void saveBean(T entity) throws Exception {
        em.merge(entity);
    }

    /**
     * @see GenericDao#delete(java.lang.Object)
     */
    public void deleteBean(T entity) throws Exception {
        em.remove(entity);
    }

    /**
     * @see GenericDao#deleteById(java.lang.Object)
     */
    public void deleteBeanById(final ID id) throws Exception {
        T entity = em.find(persistentClass, id);
        if (entity != null) {
            em.remove(entity);
        }
    }

    public void deleteBeans(ID ids[]) throws Exception {
        int size = ids.length;

        for (int idx = 0; idx < size; idx++) {
            T entity = em.find(persistentClass, ids[idx]);
            if (entity != null) {
                em.remove(entity);
            }
        }

    }

    /**
     * @see GenericDao#deleteBatchById(java.lang.Object)
     */
    public void deleteBatchById(ID ids[]) throws Exception {
        int size = ids.length;

        for (int idx = 0; idx < size; idx++) {
            T entity = em.find(persistentClass, ids[idx]);
            if (entity != null) {
                em.remove(entity);
            }
        }

    }

    /**
     * @see GenericDao#findById(java.io.Serializable)
     */
    public T findBeanById(final ID id) {
        return em.find(persistentClass, id);
    }

    /* (non-Javadoc)
	 * @see com.systech.fm.dao.generic.GenericDAO#getReference(java.io.Serializable)
     */
    public T getReference(final ID id) {
        return em.getReference(persistentClass, id);
    }

    /**
     * @see GenericDao#findByNamedQuery(String, Object[])
     * #findByNamedQuery(java.lang.String, java.lang.Object[])
     */
    @SuppressWarnings("unchecked")
    public List<T> findByNamedQuery(final String name, Object... params) {
        javax.persistence.Query query = em.createNamedQuery(name);

        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }

        final List<T> result = (List<T>) query.getResultList();
        return result;
    }

    /**
     * @see GenericDao#findByNamedQuery(String, Map)
     */
    @SuppressWarnings("unchecked")
    public List<T> findByNamedQuery(final String name,
            final Map<String, ? extends Object> params) {
        javax.persistence.Query query = em.createNamedQuery(name);

        for (final Map.Entry<String, ? extends Object> param : params.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }

        return query.getResultList();
    }

    /**
     * Use this inside subclasses as a convenience method.
     */
    @Override
    public List<T> findAll(int firstResult, final int maxResults) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(getEntityClass());
        Root<T> root = criteria.from(getEntityClass());

        CriteriaQuery<T> select = criteria.select(root);
        TypedQuery<T> typedQuery = em.createQuery(select);

        List<T> thelist = typedQuery.getResultList();

        if (firstResult > 0 || maxResults > 0) {
            while (firstResult < thelist.size()) {
                typedQuery.setFirstResult(firstResult - 1);
                typedQuery.setMaxResults(maxResults);
                firstResult += maxResults;
            }
        }

        return typedQuery.getResultList();
    }

    public List<T> list(final int firstResult, final int maxResults) {
        try {
            Query qry = em.createQuery("from " + getEntityClass().getName());
            qry.setFirstResult(firstResult);
            qry.setMaxResults(maxResults);
            return qry.getResultList();
        } catch (javax.persistence.NoResultException ex) {
            log.warn("Could not find any results with the query\"from " + getEntityClass().getName());
        }

        return new ArrayList<T>();

    }


    /* (non-Javadoc)
	 * @see com.systech.fm.dao.generic.GenericDAO#findByNamedQueryAndNamedParams(java.lang.String, java.util.Map)
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<T> findByNamedQueryAndNamedParams(final String name,
            final Map<String, ? extends Object> params) {
        javax.persistence.Query query = em.createNamedQuery(name);

        for (final Map.Entry<String, ? extends Object> param : params.entrySet()) {
            query.setParameter(param.getKey(), param.getValue());
        }

        final List<T> result = (List<T>) query.getResultList();
        return result;
    }

    @Override
    public Integer findDuplicate(final ID id, String paramFieldName, String paramFieldValue) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(getEntityClass());
        Root<T> root = criteria.from(getEntityClass());

        List<Predicate> predicates = new ArrayList<Predicate>();
        if (id != null) {
            predicates.add(builder.notEqual(root.<Long>get("id"), id));
        }
        if (paramFieldName != null && paramFieldValue != null) {
            predicates.add(builder.equal(root.<String>get(paramFieldName), paramFieldValue));
        }

        if (predicates.size() > 0) {
            criteria.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        }

        CriteriaQuery<T> select = criteria.select(root);
        TypedQuery<T> typedQuery = em.createQuery(select);

        List<T> thelist = typedQuery.getResultList();
        return thelist.size();

    }

    @Override
    public List<T> findByCriteria(int firstResult, int maxResults, CriteriaQuery<T> select) {

        TypedQuery<T> typedQuery = em.createQuery(select);

        List<T> thelist = typedQuery.getResultList();

        if (firstResult > 0 || maxResults > 0) {
            while (firstResult < thelist.size()) {
                typedQuery.setFirstResult(firstResult - 1);
                typedQuery.setMaxResults(maxResults);
                firstResult += maxResults;
            }
        }

        return typedQuery.getResultList();
    }

    @Override
    public String wrapToLike(String value) {
        return value == null ? "%" : "%" + value + "%";
    }

    @Override
    public CriteriaBuilder createCriteriaBuilder() {
        return (CriteriaBuilder) this.em.getCriteriaBuilder();
    }

    @Override
    public CriteriaQuery<T> createCriteriaQuery() {
        CriteriaQuery<T> criteria = this.createCriteriaBuilder().createQuery(getEntityClass());
        return criteria;
    }

    @Override
    public Root<T> createCriteriaRoot() {
        Root<T> root = this.createCriteriaQuery().from(getEntityClass());
        return root;
    }

    @Override
    public List<T> findByCriteria2(int firstResult, int maxResults, List<Predicate> predicates) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(getEntityClass());
        Root<T> root = criteria.from(getEntityClass());

        CriteriaQuery<T> select = criteria.select(root).where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
        TypedQuery<T> typedQuery = em.createQuery(select);

        List<T> thelist = typedQuery.getResultList();

        if (firstResult > 0 || maxResults > 0) {
            while (firstResult < thelist.size()) {
                typedQuery.setFirstResult(firstResult - 1);
                typedQuery.setMaxResults(maxResults);
                firstResult += maxResults;
            }
        }

        return typedQuery.getResultList();
    }

}
