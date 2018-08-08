/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(
        {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SelectPart {
    /**
     * JPQL 查询语句
     */
    @AliasFor("select")
    String value() default "";

    /**
     * JPQL 查询语句
     */
    @AliasFor("value")
    String select() default "";

}
