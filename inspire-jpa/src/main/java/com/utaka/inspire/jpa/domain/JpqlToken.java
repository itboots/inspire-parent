/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.domain;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * 定义{JPQL}的一部分.用于构造JPQL语句.
 *
 * @author XINEN
 */
public class JpqlToken implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String jpqlString;

    /**
     * 构造一个新的对象实例.
     *
     * @param jpqlString {@code JPQL}字符串
     */
    public JpqlToken(String jpqlString) {
        this.jpqlString = jpqlString;
    }


    /**
     * 获取{@code JPQL}字符串
     */
    public String getJpqlString() {
        return jpqlString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.jpqlString;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JpqlToken jpqlPart = (JpqlToken) o;

        return !(jpqlString != null ? !jpqlString.equals(jpqlPart.jpqlString) : jpqlPart.jpqlString != null);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(jpqlString);

    }

    /**
     * 克隆当前对象，返回一个新的对象实例.
     */
    @Override
    public JpqlToken clone() {
        JpqlToken copy = new JpqlToken(this.jpqlString);
        return copy;
    }

}
