/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.domain;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * 定义过滤条件.用于构造JPQL语句.
 *
 * @author XINEN
 */
public class ParamToken extends JpqlToken implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String name;
    private Object value;

    public ParamToken(String where) {
        super(where);
        this.name = null;
    }

    public ParamToken(String name, String where) {
        super(where);
        this.name = name;
    }

    /**
     * 构造一个新的对象实例.
     *
     * @param name  参数名
     * @param where 过滤条件
     * @param value 参数值
     */
    public ParamToken(String name, String where, Object value) {
        this(name, where);
        this.setValue(value);

    }

    /**
     * 获取过滤参数名称
     */
    public String getName() {
        return name;
    }

    /**
     * 获取过滤条件
     */
    public String getWhere() {
        return super.getJpqlString();
    }

    /**
     * 获取过滤条件的参数值
     */
    public Object getValue() {
        return value;
    }

    /**
     * 设置当前过滤条件的参数值
     */
    public ParamToken setValue(Object value) {
        this.value = value;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return Objects.toStringHelper(this.getClass())
                .add("name", this.getName())
                .add("where", this.getWhere())
                .add("value", value).toString();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParamToken)) return false;
        if (!super.equals(o)) return false;
        ParamToken that = (ParamToken) o;
        return Objects.equal(name, that.name) &&
                Objects.equal(value, that.value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(this.getName(), this.getWhere(), this.getValue());

    }
}
