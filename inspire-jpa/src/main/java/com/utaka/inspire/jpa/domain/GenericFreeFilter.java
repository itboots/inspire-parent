/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.domain;

/**
 * 定于通用的过滤条件，通过类型参数指定参数值类型。
 *
 * @author XINEN
 */
public class GenericFreeFilter<T> extends FreeFilter {

    private static final long serialVersionUID = 1L;

    public GenericFreeFilter(String where) {
        super(where);
    }

    public GenericFreeFilter(String name, String where) {
        super(name, where);
    }

    public GenericFreeFilter(String name, String where, T value) {
        super(name, where, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getValue() {
        return (T) (super.getValue());
    }

}
