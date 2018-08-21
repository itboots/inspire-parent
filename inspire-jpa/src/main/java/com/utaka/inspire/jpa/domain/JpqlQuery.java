/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.domain;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 定义一个JQPL查询。name 表示查询一个JPQL查询语句，值表示JPQL语句的命名参数。
 *
 * @author XINEN
 */
public class JpqlQuery extends JpqlToken {
    private static final long serialVersionUID = 1L;

    private final Map<String, Object> parameters;

    public JpqlQuery(String jpqlString) {
        super(jpqlString);
        this.parameters = Maps.newHashMap();
    }

    public JpqlQuery(String jpqlString, Map<String, Object> parameters) {
        super(jpqlString);
        this.parameters = parameters;
    }

    public Map<String, Object> getParameters() {
        return this.parameters;
    }

}
