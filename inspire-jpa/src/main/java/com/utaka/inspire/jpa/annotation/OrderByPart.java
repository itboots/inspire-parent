/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Locale;

/**
 * 指定排序
 */
@Target(
        {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderByPart {
    /**
     * 配许字段
     */
    @AliasFor("direction")
    Direction value() default Direction.ASC;

    @AliasFor("value")
    Direction direction() default Direction.ASC;

    enum Direction {

        /**
         * 正序，从小到大
         */
        ASC,
        /**
         * 倒序，从大到小
         */
        DESC;

        /**
         * Returns the {@link Direction} enum for the given {@link String} value.
         *
         * @param value
         * @return
         * @throws IllegalArgumentException in case the given value cannot be parsed into an enum value.
         */
        public static Direction fromString(String value) {

            try {
                return Direction.valueOf(value.toUpperCase(Locale.US));
            } catch (Exception e) {
                throw new IllegalArgumentException(String.format(
                        "Invalid value '%s' for orders given! Has to be either 'desc' or 'asc' (case insensitive).", value), e);
            }
        }

        /**
         * Returns the {@link Direction} enum for the given {@link String} or null if it cannot be parsed into an enum
         * value.
         *
         * @param value
         * @return
         */
        public static Direction fromStringOrNull(String value) {

            try {
                return fromString(value);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }
}
