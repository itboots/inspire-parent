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
 * 指定某个字段为过滤条件部件
 */
@Target(
        {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FilterPart {

    /**
     * 匹配模式
     *
     * @author XINEN
     */
    enum MatchPattern {
        /**
         * 精确匹配
         */
        None,
        /**
         * 左匹配，相当于 xxx%
         */
        Left,

        /**
         * 右匹配，相当于 %xxx
         */
        Right,

        /**
         * 全匹配，相当于 %xxx%
         */
        FullText
    }

    /**
     * 对应的过滤条件名称
     */
    @AliasFor("where")
    String value() default "";

    /**
     * 命名参数名称
     */
    String name() default "";

    /**
     * 对应的过滤条件名称
     */
    @AliasFor("value")
    String where() default "";

    /**
     * 字段名称作为过滤条件的值
     */
    String filterValue() default "";

    /**
     * 参数的值类型
     */
    Class<?> filterValueType() default String.class;

    /**
     * 模糊匹配模式
     */
    MatchPattern pattern() default MatchPattern.None;

    /**
     * 是否是常量
     */
    boolean constant() default false;

}
