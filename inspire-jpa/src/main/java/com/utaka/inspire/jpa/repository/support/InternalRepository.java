/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.repository.support;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.utaka.inspire.context.SchemaAware;
import com.utaka.inspire.jpa.domain.AbstractObject;
import com.utaka.inspire.jpa.domain.JpqlQuery;
import com.utaka.inspire.jpa.repository.SimpleRepository;
import com.utaka.inspire.jpa.util.JpaUtils;
import com.utaka.inspire.jpa.util.JpqlUtils;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 基于JPA实现简单的持久化操作。
 *
 * @author XINEN
 */
@Transactional(readOnly = true, rollbackFor = Throwable.class)
abstract class InternalRepository implements SimpleRepository {
    protected int batchSize = 30;

    protected EntityManager em;

    protected SchemaAware schemaAware;

    public InternalRepository() {

    }

    protected InternalRepository(EntityManager em, SchemaAware schemaAware) {
        this.em = em;
        this.schemaAware = schemaAware;
        Map<String, Object> props = this.em.getProperties();
        if (props.containsKey("hibernate.jdbc.batch_size")) {
            this.batchSize = Integer.parseInt(props.get("hibernate.jdbc.batch_size").toString());
        }
    }

    /**
     * 获取 Session
     */
    protected Session getSession() {
        return JpaUtils.getSession(em);

    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.SimpleQueryRepository#find(java.lang.Class,
     * java.lang.String)
     */
    @Override
    public <T> T find(Class<T> entityClass, String primaryKey) {
        return em.find(entityClass, primaryKey);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.SimpleRepository#findByQuery(java.lang.String)
     */
    @Override
    public <T> List<T> findByQuery(String queryString) {
        return findByQuery(queryString, (Map<String, Object>) null);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.SimpleRepository#findByQuery(java.lang.String,
     * java.util.Map)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findByQuery(String queryString, Map<String, Object> params) {
        Query query = this.createQuery(queryString, params);
        return query.getResultList();
    }

    @Override
    public <T> Slice<T> sliceByQuery(String queryString, Pageable pageable) {
        return sliceByQuery(queryString, (Map<String, Object>) null, pageable);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.SimpleRepository#findByQuery(java.lang.String,
     * com.dragonsoft.clear.domain.Pageable)
     */
    @Override
    public <T> Page<T> findByQuery(String queryString, Pageable pageable) {
        return findByQuery(queryString, (Map<String, Object>) null, pageable);
    }

    @Override
    public <T> Slice<T> sliceByQuery(String queryString, Map<String, Object> params, Pageable pageable) {
        return (Slice<T>) (pageable == null
                ? new SliceImpl<T>((List<T>) this.findByQuery(queryString, params))
                : readSlice(queryString, params, pageable));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.SimpleRepository#findByQuery(java.lang.String,
     * java.util.Map, com.dragonsoft.clear.domain.Pageable)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> Page<T> findByQuery(String queryString, Map<String, Object> params, Pageable pageable) {
        return (Page<T>) (pageable == null
                ? new PageImpl<T>((List<T>) this.findByQuery(queryString, params))
                : readPage(queryString, params, pageable));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.SimpleRepository#findOneByQuery(java.lang.String)
     */
    @Override
    public <T> T findOneByQuery(String queryString) {
        return findOneByQuery(queryString, (Map<String, Object>) null);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.SimpleRepository#findOneByQuery(java.lang.String,
     * java.util.Map)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T findOneByQuery(String queryString, Map<String, Object> params) {
        Query query = this.createQuery(queryString, params);
        return (T) query.getSingleResult();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.SimpleRepository#findByNativeQuery(java.lang.Class,
     * java.lang.String)
     */
    @Override
    public <T> List<T> findByNativeQuery(Class<T> resultClass, String sqlString) {
        return findByNativeQuery(resultClass, sqlString, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.SimpleRepository#findByNativeQuery(java.lang.Class,
     * java.lang.String, java.util.Map)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findByNativeQuery(Class<T> resultClass, String sqlString,
                                         Map<String, Object> params) {
        Query query = this.createNativeQuery(resultClass, sqlString, params);
        return query.getResultList();
    }

    @Override
    public <T> Slice<T> sliceByNativeQuery(Class<T> resultClass, String sqlString, Map<String, Object> params, Pageable pageable) {
        return (Slice<T>) (pageable == null
                ? new SliceImpl<T>(this.findByNativeQuery(resultClass, sqlString, params))
                : readNativeSlice(resultClass, sqlString, params, pageable));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.SimpleRepository#findByNativeQuery(java.lang.Class,
     * java.lang.String, java.util.Map, com.dragonsoft.clear.domain.Pageable)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> Page<T> findByNativeQuery(Class<T> resultClass, String sqlString,
                                         Map<String, Object> params, Pageable pageable) {
        return (Page<T>) (pageable == null
                ? new PageImpl<T>(this.findByNativeQuery(resultClass, sqlString, params))
                : readNativePage(resultClass, sqlString, params, pageable));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.SimpleRepository#findByNativeQuery(java.lang.String)
     */
    @Override
    public <T> List<T> findByNativeQuery(String sqlString) {
        return findByNativeQuery(sqlString, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.SimpleRepository#findByNativeQuery(java.lang.String,
     * java.util.Map)
     */

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findByNativeQuery(String sqlString, Map<String, Object> params) {
        NativeQuery query = this.createNativeQuery(sqlString, params);
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return (List<T>) query.list();
    }

    @Override
    public <T> Slice<T> sliceByNativeQuery(String sqlString, Map<String, Object> params, Pageable pageable) {
        return (Slice<T>) (pageable == null
                ? new SliceImpl<T>((List<T>) this.findByNativeQuery(sqlString, params))
                : readNativeSlice(sqlString, params, pageable));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.SimpleRepository#findByNativeQuery(java.lang.String,
     * java.util.Map, com.dragonsoft.clear.domain.Pageable)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> Page<T> findByNativeQuery(String sqlString, Map<String, Object> params,
                                         Pageable pageable) {
        return (Page<T>) (pageable == null
                ? new PageImpl<T>((List<T>) this.findByNativeQuery(sqlString, params))
                : readNativePage(sqlString, params, pageable));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.SimpleRepository#findOneByNativeQuery(java.lang.String)
     */
    @Override
    public <T> T findOneByNativeQuery(String sqlString) {
        return this.findOneByNativeQuery(sqlString, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.SimpleRepository#findOneByNativeQuery(java.lang.String,
     * java.util.Map)
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T findOneByNativeQuery(String sqlString, Map<String, Object> params) {
        NativeQuery query = this.createNativeQuery(sqlString, params);
        return (T) query.uniqueResult();
    }

    /*
     * (non-Javadoc)
     *
     */
    @Override
    public long countByQuery(String countQueryString) {
        return Long.valueOf(this.findOneByQuery(countQueryString).toString());
    }

    /*
     * (non-Javadoc)
     *
     */
    @Override
    public long countByQuery(String countQueryString, Map<String, Object> params) {
        return Long.valueOf(this.findOneByQuery(countQueryString, params).toString());
    }

    /*
     * (non-Javadoc)
     *
     */
    @Override
    public long countByNativeQuery(String countSqlString) {
        return Long.valueOf(this.findOneByNativeQuery(countSqlString).toString());
    }

    /*
     * (non-Javadoc)
     *
     */
    @Override
    public long countByNativeQuery(String countSqlString, Map<String, Object> params) {
        return Long.valueOf(this.findOneByNativeQuery(countSqlString, params).toString());
    }

    private static final String NATIVE_SEQUENCE_SQL_FORMATTER = "SELECT %s.nextval FROM dual";

    /*
     * (non-Javadoc)
     *
     */

    @Override
    public long countNextValFrom(String sequence) {
        return this.countByNativeQuery(
                String.format(NATIVE_SEQUENCE_SQL_FORMATTER, sequence));
    }

    /*
     * (non-Javadoc)
     *
     */
    @Override
    public int executeNativeQuery(String sqlString) {
        return this.execute(sqlString, null);

    }

    /*
     * (non-Javadoc)
     *
     */
    @Override
    public int executeNativeQuery(String sqlString, Map<String, Object> params) {
        return this.execute(sqlString, params);

    }

    public int execute(String sqlString, Map<String, Object> params) {
        NativeQuery query = this.createNativeQuery(sqlString, params);
        return query.executeUpdate();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.data.jpa.repository.JpaRepository#saveAndFlush(java.lang.Object)
     */
    @Override
    @Transactional
    public <T extends AbstractObject> T save(T entity) {
        T result = saveInternal(entity);
        flush();

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.data.jpa.repository.JpaRepository#save(java.lang.Iterable)
     */
    @Transactional
    public <T extends AbstractObject> List<T> save(Iterable<T> entities) {

        List<T> result = new ArrayList<T>();

        if (entities == null) {
            return result;
        }
        int count = 1;
        for (T entity : entities) {
            result.add(saveInternal(entity));
            if ((count++ % this.batchSize) == 0) {
                flush();
            }
        }
        flush();

        return result;
    }


    @Transactional
    protected <T extends AbstractObject> T saveInternal(T entity) {
        prepare(entity);

        if (entity.isNew()) {
            em.persist(entity);
            return entity;
        } else {
            return em.merge(entity);
        }
    }

    /*
     * (non-Javadoc)
     *
     */
    @Override
    public <T> void delete(Class<T> entityClass, String id) {
        Assert.notNull(id, "The given id must not be null!");

        delete(find(entityClass, id));
    }

    /*
     * (non-Javadoc)
     *
     */
    @Override
    public <T> void delete(T entity) {
        Assert.notNull(entity, "The entity must not be null!");
        em.remove(em.contains(entity) ? entity : em.merge(entity));

    }

    /*
     * (non-Javadoc)
     *
     */
    @Override
    public <T> void delete(Iterable<? extends T> entities) {
        Assert.notNull(entities, "The given Iterable of entities not be null!");

        for (T entity : entities) {
            delete(entity);
        }

    }

    /**
     * ******************** private methods ****************************
     */


    /*
     * (non-Javadoc)
     *
     * @see org.springframework.data.jpa.repository.JpaRepository#flush()
     */
    @Transactional
    protected void flush() {
        em.flush();

    }

    void prepare(Object entity) {
//        if (entity instanceof AuditorAware) {
//            ((AuditorAware) entity).audit(AuditorAware.DEFAULT_AUDITOR);
//        }
    }

    private Query createQuery(String queryString, Map<String, Object> params) {
        Query query = em.createQuery(queryString);
        setParameters(query, params);
        return query;
    }

    private Query createNativeQuery(Class<?> entityClass, String sqlString,
                                    Map<String, Object> params) {
        Query query = em.createNativeQuery(sqlString, entityClass);
        setParameters(query, params);

        return query;
    }

    private NativeQuery createNativeQuery(String sqlString, Map<String, Object> params) {
        NativeQuery query = getSession().createNativeQuery(sqlString);
        setParameters(query, params);
        return query;
    }

    private void setParameters(Query query, Map<String, Object> params) {
        if (params != null) {
            for (Map.Entry<String, Object> entity : params.entrySet()) {
                query.setParameter(entity.getKey(), entity.getValue());
            }
        }
    }

    private void setParameters(NativeQuery query, Map<String, Object> params) {
        if (params != null) {
            for (Map.Entry<String, Object> entity : params.entrySet()) {
                query.setParameter(entity.getKey(), entity.getValue());
            }
        }
    }

    /**
     * Reads the given {@link TypedQuery} into a {@link Page} applying the given {@link Pageable}
     * and {@link Specification}.
     *
     * @param queryString must not be {@literal null}.
     * @param params      can be {@literal null}.
     * @param pageable    can be {@literal null}.
     * @return
     */
    private <T> Page<T> readPage(String queryString, Map<String, Object> params, Pageable pageable) {
        String countQuery = JpqlUtils.createCountQueryFor(queryString);
        Long total = executeCountQuery(createQuery(countQuery, params));

        List content = Collections.emptyList();
        if (total > pageable.getOffset()) {
            content = readSliceContent(queryString, params, pageable);
        }

        return new PageImpl<T>(content, pageable, total);
    }


    @SuppressWarnings("unchecked")
    private <T> Page<T> readNativePage(String sqlString, Map<String, Object> params, Pageable pageable) {
        String countQuery = JpqlUtils.createNativeCountQueryFor(sqlString);
        Long total = executeCountQuery(createNativeQuery(countQuery, params));

        List<T> content = Collections.emptyList();
        if (total > pageable.getOffset()) {
            sqlString = JpqlUtils.applyNativeSorting(sqlString, pageable.getSort());
            NativeQuery query = this.createNativeQuery(sqlString, params);
            query.setFirstResult(Long.valueOf(pageable.getOffset()).intValue());
            query.setMaxResults(pageable.getPageSize());
            query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            content = query.list();
        }
        return new PageImpl<T>(content, pageable, total);
    }

    private <T> Page<T> readNativePage(Class<?> resutClass,
                                       String sqlString,
                                       Map<String, Object> params,
                                       Pageable pageable) {
        String countQuery = JpqlUtils.createNativeCountQueryFor(sqlString);
        Long total = executeCountQuery(createNativeQuery(countQuery, params));

        List<T> content = Collections.emptyList();
        if (total > pageable.getOffset()) {
            Query query = this.createNativeQuery(resutClass,
                    JpqlUtils.applyNativeSorting(sqlString, pageable.getSort()),
                    params);
            query.setFirstResult(Long.valueOf(pageable.getOffset()).intValue());
            query.setMaxResults(pageable.getPageSize());
            content = query.getResultList();
        }
        return new PageImpl<T>(content, pageable, total);
    }

    private <T> Slice<T> readSlice(String queryString, Map<String, Object> params, Pageable pageable) {
        List content = readSliceContent(queryString, params, pageable);
        boolean hasNext = content.size() >= pageable.getPageSize();
        return new SliceImpl<T>(content, pageable, hasNext);
    }

    private <T> Slice<T> readNativeSlice(String sqlString, Map<String, Object> params, Pageable pageable) {
        NativeQuery query = this.createNativeQuery(JpqlUtils.applyNativeSorting(sqlString, pageable.getSort()), params);
        query.setFirstResult(Long.valueOf(pageable.getOffset()).intValue());
        query.setMaxResults(pageable.getPageSize());
        query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        List content = query.list();
        content = content == null ? Collections.emptyList() : content;
        boolean hasNext = content.size() >= pageable.getPageSize();

        return new SliceImpl<T>(content, pageable, hasNext);
    }

    @SuppressWarnings({"unchecked"})
    private <T> Slice<T> readNativeSlice(Class<?> resutClass,
                                         String sqlString,
                                         Map<String, Object> params,
                                         Pageable pageable) {

        Query query = this.createNativeQuery(resutClass,
                JpqlUtils.applyNativeSorting(sqlString, pageable.getSort()),
                params);
        query.setFirstResult(Long.valueOf(pageable.getOffset()).intValue());
        query.setMaxResults(pageable.getPageSize());

        List content = query.getResultList();
        content = content == null ? Collections.emptyList() : content;
        boolean hasNext = content.size() >= pageable.getPageSize();

        return new SliceImpl<T>(content, pageable, hasNext);
    }

    private <T> List<T> readSliceContent(String queryString, Map<String, Object> params, Pageable pageable) {
        String lowerQuery = queryString.toLowerCase();
        if (lowerQuery.indexOf("fetch") > 0 && lowerQuery.indexOf("join") > 0) {
            //1： 先分页查询ID
            //2： 根据ID查询内容
            Set<String> pkList = readPkList(queryString, params, pageable);
            JpqlQuery jpqlQuery = JpqlUtils.createQueryByPrimaryKey(queryString, params, pkList);
            Query query = this.createQuery(JpqlUtils.applySorting(jpqlQuery.getJpqlString(), pageable.getSort()), jpqlQuery.getParameters());
            return query.getResultList();

        } else {
            Query query = this.createQuery(JpqlUtils.applySorting(queryString, pageable.getSort()), params);
            query.setFirstResult(Long.valueOf(pageable.getOffset()).intValue());
            query.setMaxResults(pageable.getPageSize());
            return query.getResultList();

        }
    }

    private Set<String> readPkList(String queryString, Map<String, Object> params, Pageable pageable) {
        String pkQuery = JpqlUtils.createPrimaryKeyQueryFor(queryString, pageable.getSort());

        int offset = 0;
        int size = Long.valueOf(pageable.getOffset() + pageable.getPageSize()).intValue();
        int pageSize = pageable.getPageSize() * 2;

        //自己分页，每次读取两页，去重
        Set<String> pkList = Sets.newLinkedHashSet();
        while (pkList.size() < size) {
            Query query = this.createQuery(pkQuery, params);
            query.setFirstResult(offset);
            query.setMaxResults(pageSize);
            List<String> pk = query.getResultList();
            pkList.addAll(pk);
            if (pk.size() < pageSize) {
                break;
            }
            offset += pageSize;
        }

        Iterable<String> iterable = Iterables.skip(pkList, Long.valueOf(pageable.getOffset()).intValue());
        iterable = Iterables.limit(iterable, pageable.getPageSize());
        return Sets.newLinkedHashSet(iterable);
    }

    /**
     * Executes a count query and transparently sums up all values returned.
     *
     * @param query must not be {@literal null}.
     * @return
     */
    private static Long executeCountQuery(Query query) {
        checkNotNull(query);

        return Long.valueOf(query.getSingleResult().toString());

    }

    private static Long executeCountQuery(NativeQuery query) {
        checkNotNull(query);

        return Long.valueOf(query.uniqueResult().toString());

    }

    void setCurrentSchema() {
        if (this.schemaAware != null) {
            String sql = this.schemaAware.getAlterCurrentSchemaSQL();
            if (!StringUtils.isEmpty(sql)) {
                this.execute(sql, null);
            }
        }
    }
}
