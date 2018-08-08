/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util;


import javax.annotation.Nonnull;

/**
 * 对象转换接口
 *
 * @author lanxe
 */
public interface CastFunction<T> {

    /**
     * 从指定的基本类型转化为目标基本类型
     *
     * @param s 要转变换的字符串
     * @return 返回目标类型值
     */
    T tryParse(@Nonnull String s);

}
