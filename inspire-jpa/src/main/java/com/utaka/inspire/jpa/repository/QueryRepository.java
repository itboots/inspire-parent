/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Map;

/**
 * 定义简单的查询接口。
 *
 * @author XINEN
 */
@NoRepositoryBean
interface QueryRepository {

    /**
     * 根据主键查询。
     */
    <T> T find(Class<T> entityClass, String primaryKey);

    /**
     * 根据JPQL查询语句查询，返回对象列表。
     *
     * @param queryString
     * @return 返回结果列表。
     */
    <T> List<T> findByQuery(String queryString);

    /**
     * 根据JPQL查询语句查询，返回对象列表。
     *
     * @param queryString
     * @param params      参数
     * @return 返回结果列表。
     */
    <T> List<T> findByQuery(String queryString, Map<String, Object> params);


    /**
     * 根据JPQL查询语句查询，返回对象分页列表。
     *
     * @param queryString 查询字符串
     * @param pageable    分页信息
     * @return 返回结果列表。
     */
    <T> Slice<T> sliceByQuery(String queryString, Pageable pageable);

    /**
     * 根据JPQL查询语句查询，返回对象分页列表。
     *
     * @param queryString 查询字符串
     * @param pageable    分页信息
     * @return 返回结果列表。
     */
    <T> Page<T> findByQuery(String queryString, Pageable pageable);

    /**
     * 根据JPQL查询语句查询，返回对象分页列表。
     *
     * @param queryString
     * @param params      参数
     * @return 返回结果列表。
     */
    <T> Slice<T> sliceByQuery(String queryString, Map<String, Object> params, Pageable pageable);

    /**
     * 根据JPQL查询语句查询，返回对象分页列表。
     *
     * @param queryString
     * @param params      参数
     * @return 返回结果列表。
     */
    <T> Page<T> findByQuery(String queryString, Map<String, Object> params, Pageable pageable);

    /**
     * 执行JPQL查询语句，并返回单个结果。
     *
     * @param queryString JPQL 查询字符串
     * @return 返回执行SQL后的单个结果。
     */
    <T> T findOneByQuery(String queryString);

    /**
     * 执行JPQL查询语句，并返回单个结果。
     *
     * @param queryString JPQL 查询字符串
     * @param params      参数
     * @return 返回执行SQL后的单个结果。
     */
    <T> T findOneByQuery(String queryString, Map<String, Object> params);

    /**
     * 执行JPQL查询语句，并返统一结果。
     *
     * @param countQueryString JPQL 查询字符串
     * @return 返回总记录数。
     */
    long countByQuery(String countQueryString);

    /**
     * 执行JPQL查询语句，并返统一结果。
     *
     * @param countQueryString JPQL 查询字符串
     * @param params           参数
     * @return 返回总记录数。
     */
    long countByQuery(String countQueryString, Map<String, Object> params);

    /**
     * 执行原生的SQL查询语句，返回制定对象的列表。
     *
     * @param resultClass 返回的结果类型
     * @param sqlString   原生的SQL查询字符串
     * @return 返回指定类型的结果列表。
     */
    <T> List<T> findByNativeQuery(Class<T> resultClass, String sqlString);

    /**
     * 执行原生的SQL查询语句，返回制定对象的列表。
     *
     * @param resultClass 返回的结果类型
     * @param sqlString   原生的SQL查询字符串
     * @param params      参数
     * @return 返回指定类型的结果列表。
     */
    <T> List<T> findByNativeQuery(Class<T> resultClass, String sqlString,
                                  Map<String, Object> params);

    /**
     * 执行原生的SQL查询语句，返回制定对象的列表。
     *
     * @param resultClass 返回的结果类型
     * @param sqlString   原生的SQL查询字符串
     * @param params      参数
     * @return 返回指定类型的结果列表。
     */
    <T> Slice<T> sliceByNativeQuery(Class<T> resultClass, String sqlString, Map<String, Object> params, Pageable pageable);

    /**
     * 执行原生的SQL查询语句，返回制定对象的列表。
     *
     * @param resultClass 返回的结果类型
     * @param sqlString   原生的SQL查询字符串
     * @param params      参数
     * @return 返回指定类型的结果列表。
     */
    <T> Page<T> findByNativeQuery(Class<T> resultClass, String sqlString, Map<String, Object> params, Pageable pageable);

    /**
     * 执行原生的SQL查询语句，返回制定对象的列表。
     *
     * @param sqlString 原生的SQL查询字符串
     * @return 返回指定类型的结果列表。
     */
    <T> List<T> findByNativeQuery(String sqlString);

    /**
     * 执行原生的SQL查询语句，返回制定对象的列表。
     *
     * @param sqlString 原生的SQL查询字符串
     * @param params    参数
     * @return 返回指定类型的结果列表。
     */
    <T> List<T> findByNativeQuery(String sqlString, Map<String, Object> params);

    /**
     * 执行原生的SQL查询语句，返回制定对象的列表。
     *
     * @param sqlString 原生的SQL查询字符串
     * @param params    参数
     * @return 返回指定类型的结果列表。
     */
    <T> Slice<T> sliceByNativeQuery(String sqlString, Map<String, Object> params, Pageable pageable);

    /**
     * 执行原生的SQL查询语句，返回制定对象的列表。
     *
     * @param sqlString 原生的SQL查询字符串
     * @param params    参数
     * @return 返回指定类型的结果列表。
     */
    <T> Page<T> findByNativeQuery(String sqlString, Map<String, Object> params, Pageable pageable);

    /**
     * 执行原生的SQL查询语句，并返回单个结果。
     *
     * @param sqlString 原生的SQL查询字符串
     * @return 返回执行SQL后的单个结果。
     */
    <T> T findOneByNativeQuery(String sqlString);

    /**
     * 执行原生的SQL查询语句，并返回单个结果。
     *
     * @param sqlString 原生的SQL查询字符串
     * @param params    参数
     * @return 返回执行SQL后的单个结果。
     */
    <T> T findOneByNativeQuery(String sqlString, Map<String, Object> params);

    /**
     * 执行JPQL查询语句，并返统一结果。
     *
     * @param countSqlString 原生的SQL查询字符串
     * @return 返回总记录数。
     */
    long countByNativeQuery(String countSqlString);

    /**
     * 执行JPQL查询语句，并返统一结果。
     *
     * @param countSqlString 参数
     * @return 返回总记录数。
     */
    long countByNativeQuery(String countSqlString, Map<String, Object> params);

    /**
     * 获取序列的下一个值。
     *
     * @param sequence 要查询的序列。
     * @return 返回序列的最后一个值。
     */
    long countNextValFrom(String sequence);

    /**
     * 执行原生SQL语句。
     *
     * @param sqlString 原生的SQL查询字符串
     * @return 返回受影响的行数。
     */
    int executeNativeQuery(String sqlString);

    /**
     * 执行原生SQL语句。
     *
     * @param sqlString 原生的SQL查询字符串
     * @param params    参数
     * @return 返回受影响的行数。
     */
    int executeNativeQuery(String sqlString, Map<String, Object> params);

}
