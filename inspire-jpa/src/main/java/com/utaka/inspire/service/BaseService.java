/*
 * Copyright (c) 2014, Inspireso and/or its affiliates. All rights reserved.
 */

package com.utaka.inspire.service;

import com.utaka.inspire.data.jpa.domain.AbstractObject;
import com.utaka.inspire.event.DefaultEventBusService;
import com.utaka.inspire.event.EventBusService;
import com.utaka.inspire.jpa.domain.JpqlQuery;
import com.utaka.inspire.jpa.domain.JpqlToken;
import com.utaka.inspire.jpa.domain.Pages;
import com.utaka.inspire.jpa.repository.SimpleRepository;
import com.utaka.inspire.jpa.util.JpqlUtils;
import com.utaka.inspire.util.LogManager;
import com.utaka.inspire.util.Transform;
import org.slf4j.Logger;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 业务逻辑服务的基类。所有业务服务都继承此类，作为后续扩和控制的基础。
 *
 * @author LANXE
 */
@Transactional(rollbackFor = Exception.class, readOnly = true)
public abstract class BaseService implements BeanNameAware, InitializingBean {


    /**
     * 日志记录器
     */
    protected static final Logger logger = LogManager.getCurrentClassLogger();


    private String beanName;

    @Autowired
    private SimpleRepository simpleRepository;

    @Autowired(required = false)
    protected EventBusService bus;

    @Override
    public void afterPropertiesSet() throws java.lang.Exception {
        if (this.bus == null) {
            this.bus = DefaultEventBusService.INSTANCE;
        }
    }


    /**
     * {@inheritDoc}
     *
     * @see BeanNameAware#setBeanName(String)
     */
    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    /**
     * 获取当前对象的在Spring容器中的标识。
     */
    public String getBeanName() {
        return this.beanName;
    }


    /**
     * 保存，如果是新增的记录，则直接保存；否则从数据库重新获取数据并复制当前对象后更新到持久层。 如果是新增的记录，不应该给id赋任何我值；是修改的记录，则必须保证id有值并且和数据库相符。
     *
     * @param object 要保存的对象。
     * @return 返回保存后的对象。
     */
    @Transactional
    public <T extends AbstractObject> T saveOrUpdate(T object) {
        if (!object.isNew()) {
            @SuppressWarnings("unchecked") T entity =
                    (T) this.simpleRepository.find(object.getClass(), object.getId());
            object = Transform.copy(object, entity, true, false);
        }
        return this.simpleRepository.save(object);
    }

    /**
     * 根据主键删除对象。
     *
     * @param entityClass 实体类型。
     * @param id          主键值。
     */
    @Transactional
    public <T> void delete(Class<T> entityClass, String id) {
        this.simpleRepository.delete(entityClass, id);
    }

    /**
     * 删除指定的对象。
     *
     * @param entity 实体对象。
     */
    @Transactional
    public <T extends AbstractObject> void delete(T entity) {
        this.simpleRepository.delete(entity);
    }

    /**
     * 根据主键查询对象。
     *
     * @param entityClass 实体类型。
     * @param id          逻辑主键。
     * @return 如果存在此记录则直接此对象，否则返回 <code>null</code>。
     */

    public <T> T find(Class<T> entityClass, String id) {
        return this.simpleRepository.find(entityClass, id);

    }

    /**
     * 根据动态过滤条件查询。
     *
     * @param tokens JPQL 组成部分.
     * @return 返回查询结果的列表.
     */
    public <T, S extends JpqlToken> List<T> find(Iterable<S> tokens) {
        JpqlQuery query = JpqlUtils.createQuery(tokens);
        return this.simpleRepository.findByQuery(query.getJpqlString(), query.getParameters());

    }


    /**
     * 根据动态过滤条件查询.
     *
     * @param entityClass 实体类型.
     * @param tokens      JPQL 组成部分.
     * @return 返回查询结果的列表.
     */
    public <T, S extends JpqlToken> List<T> find(Class<? extends T> entityClass, Iterable<S> tokens) {
        JpqlQuery query = JpqlUtils.createQuery(entityClass, tokens);
        return this.simpleRepository.findByQuery(query.getJpqlString(), query.getParameters());

    }

    /**
     * 根据动态过滤条件查询。
     *
     * @param tokens 原生SQL 组成部分.
     * @return 返回查询结果的列表.
     */
    public <T, S extends JpqlToken> List<T> findByNativeQuery(Iterable<S> tokens) {
        JpqlQuery query = JpqlUtils.createQuery(tokens);
        return this.simpleRepository.findByNativeQuery(query.getJpqlString(), query.getParameters());

    }

    /**
     * 根据动态过滤条件查询.
     *
     * @param entityClass 实体类型.
     * @param tokens      JPQL 组成部分.
     * @return 返回查询结果的列表.
     */
    public <T, S extends JpqlToken> List<T> findDistinct(Class<? extends T> entityClass, Iterable<S> tokens) {
        JpqlQuery query = JpqlUtils.createDistinctQuery(entityClass, tokens);
        return this.simpleRepository.findByQuery(query.getJpqlString(), query.getParameters());

    }

    /**
     * 根据动态过滤条件查询。
     *
     * @param tokens JPQL 组成部分.
     * @return 返回查询结果的列表.
     */
    public <T, S extends JpqlToken> Page<T> find(Iterable<? extends S> tokens, Pageable pageable) {
        JpqlQuery query = JpqlUtils.createCountQuery(tokens);
        if (query != null) {
            long total = this.simpleRepository.countByQuery(query.getJpqlString(), query.getParameters());
            Slice<T> content = Pages.newPage();
            if (total > 0) {
                query = JpqlUtils.createQuery(tokens);
                content = this.simpleRepository.sliceByQuery(query.getJpqlString(), query.getParameters(), pageable);
            }
            return Pages.newPage(content.getContent(), pageable, total);
        } else {
            query = JpqlUtils.createQuery(tokens);
            return this.simpleRepository.findByQuery(query.getJpqlString(), query.getParameters(), pageable);
        }
    }

    /**
     * 根据动态过滤条件查询。
     *
     * @param tokens JPQL 组成部分.
     * @return 返回查询结果的列表.
     */
    public <T, S extends JpqlToken> Page<T> findByNativeQuery(Iterable<? extends S> tokens, Pageable pageable) {
        JpqlQuery query = JpqlUtils.createCountQuery(tokens);
        if (query != null) {
            long total = this.simpleRepository.countByNativeQuery(query.getJpqlString(), query.getParameters());
            Slice<T> content = Pages.newPage();
            if (total > 0) {
                query = JpqlUtils.createQuery(tokens);
                content = this.simpleRepository.sliceByNativeQuery(query.getJpqlString(), query.getParameters(), pageable);
            }
            return Pages.newPage(content.getContent(), pageable, total);
        } else {
            query = JpqlUtils.createQuery(tokens);
            return this.simpleRepository.findByNativeQuery(query.getJpqlString(), query.getParameters(), pageable);
        }
    }

    /**
     * 根据动态过滤条件查询。
     *
     * @param entityClass 实体类型.
     * @param tokens      JPQL 组成部分.
     * @return 返回查询结果的列表.
     */
    public <T, S extends JpqlToken> Page<T> find(Class<? extends T> entityClass, Iterable<S> tokens, Pageable pageable) {
        JpqlQuery query = JpqlUtils.createQuery(entityClass, tokens);
        return this.simpleRepository.findByQuery(query.getJpqlString(), query.getParameters(), pageable);

    }

    /**
     * 根据动态过滤条件查询。
     *
     * @param entityClass 实体类型.
     * @param tokens      JPQL 组成部分.
     * @return 返回查询结果的列表.
     */
    public <T, S extends JpqlToken> Page<T> findDistinct(Class<? extends T> entityClass, Iterable<S> tokens, Pageable pageable) {
        JpqlQuery query = JpqlUtils.createDistinctQuery(entityClass, tokens);
        return this.simpleRepository.findByQuery(query.getJpqlString(), query.getParameters(), pageable);

    }
}
