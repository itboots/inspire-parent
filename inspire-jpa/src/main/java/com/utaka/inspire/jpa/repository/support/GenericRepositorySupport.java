/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.repository.support;

import com.utaka.inspire.context.SchemaAware;
import com.utaka.inspire.jpa.domain.AbstractObject;
import com.utaka.inspire.jpa.repository.DynamicSchemaSupport;
import com.utaka.inspire.jpa.repository.GenericRepository;
import com.utaka.inspire.jpa.util.JpaUtils;
import org.hibernate.Session;
import org.springframework.beans.BeansException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link GenericRepository} 的模式实现。工厂以此为基础构造所有的Repository对象。
 *
 * @author XINEN
 */
@Transactional(readOnly = true, rollbackFor = Throwable.class)
public class GenericRepositorySupport<T>
        extends SimpleJpaRepository<T, String>
        implements GenericRepository<T>, DynamicSchemaSupport {

    @NoRepositoryBean
    private class DefaultSimpleRepository extends InternalRepository {
        public DefaultSimpleRepository(EntityManager em, SchemaAware schemaAware) {
            super(em, schemaAware);
        }

    }

    private EntityManager em;
    private DefaultSimpleRepository support;
    private SchemaAware schemaAware;

    /**
     * Creates a new {@link GenericRepositorySupport} to manage objects of the given {@link JpaEntityInformation}.
     *
     * @param entityInformation must not be {@literal null}.
     * @param entityManager     must not be {@literal null}.
     */
    public GenericRepositorySupport(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.init(entityManager);
    }

    /**
     * Creates a new {@link GenericRepositorySupport} to manage objects of the given domain type.
     *
     * @param domainClass   must not be {@literal null}.
     * @param entityManager must not be {@literal null}.
     */
    public GenericRepositorySupport(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.init(entityManager);
    }


    /*
     * (non-Javadoc)
     *
     */
    @Override
    public void alterCurrentSchema() {
        this.support.setCurrentSchema();

    }

    /**
     * 获取 Session
     */
    protected Session getSession() {
        return JpaUtils.getSession(em);

    }

    //
    // /**
    // * 获取 Dialect
    // */
    // protected Dialect getDialect() {
    // return this.dialect;
    //
    // }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.SimpleQueryRepository#find(java.lang.Class,
     * java.lang.String)
     */
    @Override
    public <S> S find(Class<S> entityClass, String primaryKey) {
        return em.find(entityClass, primaryKey);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.ClearRepository#findByQuery(java.lang .String)
     */
    @Override
    public <S> List<S> findByQuery(String queryString) {
        return this.support.findByQuery(queryString);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.ClearRepository#findByQuery( java.lang .String)
     */
    @Override
    public <S> List<S> findByQuery(String queryString, Map<String, Object> params) {
        return this.support.findByQuery(queryString, params);
    }

    @Override
    public <T> Slice<T> sliceByQuery(String queryString, Pageable pageable) {
        return this.support.sliceByQuery(queryString, pageable);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.ClearRepository#findByQuery(java.lang .String,
     * org.springframework.data.domain.Pageable)
     */
    @Override
    public <S> Page<S> findByQuery(String queryString, Pageable pageable) {
        return this.support.findByQuery(queryString, pageable);
    }

    @Override
    public <T> Slice<T> sliceByQuery(String queryString, Map<String, Object> params, Pageable pageable) {
        return this.support.sliceByQuery(queryString, params, pageable);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.ClearRepository#findByQuery(java.lang.String,
     * java.util.Map, org.springframework.data.domain.Pageable)
     */
    @Override
    public <S> Page<S> findByQuery(String queryString, Map<String, Object> params, Pageable pageable) {
        return this.support.findByQuery(queryString, params, pageable);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.ClearRepository#findOneByQuery(java.lang .String)
     */
    @Override
    public <S> S findOneByQuery(String queryString) {
        return this.support.findOneByQuery(queryString);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.ClearRepository#findOneByQuery( java.lang .String,
     * java.util.Map)
     */
    @Override
    public <S> S findOneByQuery(String queryString, Map<String, Object> params) {
        return this.support.findOneByQuery(queryString, params);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.ClearRepository#findByNativeQuery(java.lang.Class,
     * java.lang.String)
     */
    @Override
    public <S> List<S> findByNativeQuery(Class<S> resultClass, String sqlString) {
        return this.support.findByNativeQuery(resultClass, sqlString);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.ClearRepository#findByNativeQuery(java.lang.Class,
     * java.lang.String, java.util.Map)
     */
    @Override
    public <S> List<S> findByNativeQuery(Class<S> resultClass, String sqlString, Map<String, Object> params) {
        return this.support.findByNativeQuery(resultClass, sqlString, params);
    }

    @Override
    public <T> Slice<T> sliceByNativeQuery(Class<T> resultClass, String sqlString, Map<String, Object> params, Pageable pageable) {
        return this.support.sliceByNativeQuery(resultClass, sqlString, params, pageable);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.ClearRepository#findByNativeQuery(java.lang.Class,
     * java.lang.String, java.util.Map, com.dragonsoft.clear.domain.Pageable)
     */
    @Override
    public <S> Page<S> findByNativeQuery(Class<S> resultClass, String sqlString,
                                         Map<String, Object> params, Pageable pageable) {
        return this.support.findByNativeQuery(resultClass, sqlString, params, pageable);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.ClearRepository#findByNativeQuery(java.lang .String)
     */
    @Override
    public <S> List<S> findByNativeQuery(String sqlString) {
        return this.support.findByNativeQuery(sqlString);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.ClearRepository#findByNativeQuery( java.lang .String)
     */
    @Override
    public <S> List<S> findByNativeQuery(String sqlString, Map<String, Object> params) {
        return this.support.findByNativeQuery(sqlString, params);
    }

    @Override
    public <T> Slice<T> sliceByNativeQuery(String sqlString, Map<String, Object> params, Pageable pageable) {
        return this.support.sliceByNativeQuery(sqlString, params, pageable);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.ClearRepository#findByNativeQuery(java.lang .String,
     * java.util.Map, org.springframework.data.domain.Pageable)
     */
    @Override
    public <S> Page<S> findByNativeQuery(String sqlString, Map<String, Object> params,
                                         Pageable pageable) {
        return this.support.findByNativeQuery(sqlString, params, pageable);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.ClearRepository#findOneByNativeQuery( java.lang.String)
     */
    @Override
    public <S> S findOneByNativeQuery(String sqlString) {
        return this.support.findOneByNativeQuery(sqlString);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.dragonsoft.clear.repository.ClearRepository#findOneByNativeQuery( java.lang .String)
     */
    @Override
    public <S> S findOneByNativeQuery(String sqlString, Map<String, Object> params) {
        return this.support.findOneByNativeQuery(sqlString, params);
    }

    /*
     * (non-Javadoc)
     *
     */
    @Override
    public long countByQuery(String countQueryString) {
        return this.support.countByQuery(countQueryString);
    }

    /*
     * (non-Javadoc)
     *
     */
    @Override
    public long countByQuery(String countQueryString, Map<String, Object> params) {
        return this.support.countByQuery(countQueryString, params);
    }

    /*
     * (non-Javadoc)
     *
     */
    @Override
    public long countByNativeQuery(String countSqlString) {
        return this.support.countByNativeQuery(countSqlString);
    }

    /*
     * (non-Javadoc)
     *
     */
    @Override
    public long countByNativeQuery(String countSqlString, Map<String, Object> params) {
        return this.support.countByNativeQuery(countSqlString, params);
    }

    /*
     * (non-Javadoc)
     *
     */
    @Override
    public long countNextValFrom(String sequence) {
        return this.support.countNextValFrom(sequence);
    }

    /*
     * (non-Javadoc)
     *
     */
    @Override
    public int executeNativeQuery(String sqlString) {
        return this.support.executeNativeQuery(sqlString, null);

    }

    /*
     * (non-Javadoc)
     *
     */
    @Override
    public int executeNativeQuery(String sqlString, Map<String, Object> params) {
        return this.support.executeNativeQuery(sqlString, params);

    }

    @Transactional
    @Override
    public <S extends T> S save(S entity) {
        S result = this.saveInternal(entity);
        this.flush();
        return result;

    }


    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.JpaRepository#save(java.lang.Iterable)
     */
    @Transactional(rollbackFor = Throwable.class)
    public <S extends T> List<S> save(Iterable<S> entities) {

        List<S> result = new ArrayList<S>();

        if (entities == null) {
            return result;
        }
        int count = 1;
        for (S entity : entities) {
            result.add(saveInternal(entity));
            if ((count++ % this.support.batchSize) == 0) {
                this.flush();
            }
        }
        this.flush();
        return result;
    }

    /**
     * ********************* protected methods ****************************
     */

    private void init(EntityManager entityManager) throws BeansException {
        this.em = entityManager;
        this.support = new DefaultSimpleRepository(this.em, this.schemaAware);
    }

    private <S extends T> S saveInternal(S entity) {
        S result = null;
        if (entity instanceof AbstractObject) {
            AbstractObject ae = (AbstractObject) entity;
            result = (S) this.support.saveInternal(ae);
        } else {
            this.support.prepare(entity);
            result = super.save(entity);
        }
        return result;

    }


}
