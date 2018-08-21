/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.repository;

import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * 定义简单的更新接口。
 *
 * @author XINEN
 */
@NoRepositoryBean
public interface ModifingRepository {

    /**
     * 持久化至数据库；当对象是新对象时直接新增，否则执行更新操作。
     *
     * @param entity 要持久化的对象
     * @return 返回持久化之后的对象，和数据库保持一致；
     */
    <T extends Persistable> T save(T entity);

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.data.jpa.repository.JpaRepository#save(java.lang.Iterable)
     */
    <T extends Persistable> List<T> save(Iterable<T> entities);

    /**
     * Deletes the entity with the given id.
     *
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
     */
    <T> void delete(Class<T> entityClass, String id);

    /**
     * Deletes a given entity.
     *
     * @param entity
     * @throws IllegalArgumentException in case the given entity is (@literal null}.
     */
    <T> void delete(T entity);

    /**
     * Deletes the given entities.
     *
     * @param entities
     * @throws IllegalArgumentException in case the given {@link Iterable} is (@literal null}.
     */
    <T> void delete(Iterable<? extends T> entities);
}
