/*
 * Copyright (c) 2014. Inspireso and/or its affiliates.
 */

package com.utaka.inspire.jpa.domain;

import com.google.common.collect.Maps;
import com.google.common.primitives.Primitives;

import javax.annotation.Nonnull;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

final class Casting {
    private static Map<Class<?>, CastFunction<?>> functionMap = Maps.newConcurrentMap();


    static {
        functionMap.put(Boolean.class, new CastFunction<Boolean>() {

            @Override
            public Boolean tryParse(@Nonnull String s) {
                String lower = s.toLowerCase();
                s = ("0".equals(lower)
                        || "no".equals(lower)
                        || "n".equals(lower))
                        || "false".equals(lower)
                        ?
                        "false"
                        :
                        "true";

                return Boolean.parseBoolean(s);
            }

        });

        functionMap.put(Short.class, new CastFunction<Short>() {

            @Override
            public Short tryParse(@Nonnull String val) {
                return Short.parseShort(checkNotNull(val));
            }

        });

        functionMap.put(Integer.class, new CastFunction<Integer>() {

            @Override
            public Integer tryParse(@Nonnull String val) {
                return Integer.parseInt(checkNotNull(val));
            }

        });

        functionMap.put(Long.class, new CastFunction<Long>() {

            @Override
            public Long tryParse(@Nonnull String val) {
                return Long.parseLong(checkNotNull(val));
            }

        });

        functionMap.put(Float.class, new CastFunction<Float>() {

            @Override
            public Float tryParse(@Nonnull String val) {
                return Float.parseFloat(checkNotNull(val));
            }

        });

        functionMap.put(Double.class, new CastFunction<Double>() {

            @Override
            public Double tryParse(@Nonnull String s) {
                return Double.parseDouble(checkNotNull(s));
            }

        });

        functionMap.put(String.class, new CastFunction<String>() {

            @Override
            public String tryParse(@Nonnull String s) {
                return s;
            }

        });

        functionMap.put(java.util.Date.class, new CastFunction<java.util.Date>() {

            @Override
            public java.util.Date tryParse(@Nonnull String s) {
                DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
                try {
                    return df.parse(checkNotNull(s));
                } catch (ParseException e) {
                    return null;
                }
            }

        });
    }

    /**
     * 基本类型转换
     *
     * @param target 目标类型
     * @return 返回指定类型的值
     */
    @SuppressWarnings("unchecked")
    public static <T> CastFunction<T> to(Class<T> target) {
        Class<T> parsableType = Primitives.wrap(target);
        return functionMap.containsKey(parsableType) ? (CastFunction<T>) functionMap.get(parsableType) :
                new CastFunction<T>() {
                    @Override
                    public T tryParse(@Nonnull String s) {
                        return (T) s;
                    }

                };


    }

}
