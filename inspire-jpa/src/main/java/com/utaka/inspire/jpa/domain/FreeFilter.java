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
public class FreeFilter extends ParamToken implements Serializable {
    private static final long serialVersionUID = 1L;

    public FreeFilter(String where) {
        super(where);
    }

    public FreeFilter(String name, String where) {
        super(name, where);
    }

    /**
     * 构造一个新的对象实例.
     *
     * @param name  参数名
     * @param where 过滤条件
     * @param value 参数值
     */
    public FreeFilter(String name, String where, Object value) {
        super(name, where, value);
    }

    /**
     * 设置当前过滤条件的参数值
     */
    @Override
    public FreeFilter setValue(Object value) {
        super.setValue(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(this.getName(), this.getWhere(), this.getValue());

    }

    /**
     * 克隆当前对象，返回一个新的对象实例.
     */
    @Override
    public FreeFilter clone() {
        FreeFilter copy = Filters.newFreeFilter(this.getName(), this.getWhere());
        return copy.setValue(this.getValue());
    }

    /**
     * 使用指定的值克隆过滤条件;相同的过滤条件，使用指定的值.
     *
     * @param val 要设置的新值
     */
    public FreeFilter cloneWithValue(Object val) {
        FreeFilter copy = Filters.newFreeFilter(this.getName(), this.getWhere());
        return copy.setValue(val);
    }

}
