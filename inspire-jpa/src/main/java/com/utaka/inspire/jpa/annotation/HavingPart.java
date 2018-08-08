/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定某个字段为分组过滤条件部件
 *
 * @author lanxe
 */
@Target(
        {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HavingPart {

    /**
     * 对应的过滤条件名称
     */
    @AliasFor("having")
    String value() default "";

    /**
     * 命名参数名称
     */
    String name() default "";

    /**
     * 对应的过滤条件名称
     */
    @AliasFor("value")
    String having() default "";

    /**
     * 字段名称作为过滤条件的值
     */
    String filterValue() default "";

    /**
     * 参数的值类型
     */
    Class<?> filterValueType() default String.class;
}
