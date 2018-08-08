/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.data;

/**
 * 数据库自动切换接口
 *
 * @author LANXE
 */
public interface SchemaAware {

    /**
     * 默认数据库
     */
    static final String DEFAULT_BEAN_NAME = "schemaAware";

    /**
     * 设置当前使用的{@literal schema}
     */
    void setSchema(String schema);

    /**
     * 获取当前的schema
     */
    String getCurrentSchema();

    /**
     * 获取切换{@literal schema}的{@literal SQL}语句
     */
    String getAlterCurrentSchemaSQL();

}
