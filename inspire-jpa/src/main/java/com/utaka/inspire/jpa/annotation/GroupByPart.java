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
 * @author lanxe
 */
@Target(
        {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GroupByPart {
    /**
     * 分组字段
     */
    @AliasFor("groupBy")
    String value() default "";

    @AliasFor("value")
    String groupBy() default "";

}
