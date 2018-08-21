/*
 * Copyright (c) 2014. Inspireso and/or its affiliates.
 */

package com.utaka.inspire.jpa.domain;


import javax.annotation.Nonnull;

/**
 * 对象转换接口
 * @author LANXE
 */
@FunctionalInterface
public interface CastFunction<T> {

    /**
     * 从指定的基本类型转化为目标基本类型
     *
     * @param s 要转变换的字符串
     * @return 返回目标类型值
     */
    T tryParse(@Nonnull String s);

}
