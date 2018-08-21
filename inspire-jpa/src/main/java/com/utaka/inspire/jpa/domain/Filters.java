/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.domain;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.utaka.inspire.jpa.annotation.FilterPart;
import com.utaka.inspire.jpa.annotation.FilterPart.MatchPattern;
import com.utaka.inspire.util.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 过滤条件工具类。
 *
 * @author XINEN
 */
public final class Filters {

    /**
     * 新建一个 {@link GenericFreeFilter } 过滤条件。
     *
     * @param name  参数名称。
     * @param where 过滤条件。
     * @param value 参数值。
     * @param <T>   参数类型。
     * @return 返回一个 {@link GenericFreeFilter } 对象。
     */
    public static <T> GenericFreeFilter<T> newFreeFilter(String name, String where, T value) {
        return new GenericFreeFilter<T>(name, where, value);
    }

    /**
     * 新建一个 {@link GenericFreeFilter } 过滤条件。
     *
     * @param name  参数名称。
     * @param where 过滤条件。
     * @return 返回一个 {@link GenericFreeFilter } 对象。
     */
    public static FreeFilter newFreeFilter(String name, String where) {
        return new FreeFilter(name, where, null);
    }

    /**
     * 新建一个常量的 {@link GenericFreeFilter } 过滤条件。
     *
     * @param where 过滤条件。
     * @return 返回一个 {@link GenericFreeFilter } 对象。
     */
    public static FreeFilter newFreeFilter(String where) {
        return new FreeFilter(null, where, null);
    }

    /**
     * 把一个过滤条件列表，
     *
     * @param filters 过滤条件列表。
     */
    public static Map<String, FreeFilter> asMap(Iterable<FreeFilter> filters) {
        Map<String, FreeFilter> map = Maps.newHashMap();
        for (FreeFilter filter : filters) {
            map.put(StringUtils.isEmpty(filter.getName()) ? filter.getWhere() : filter.getName(),
                    filter);
        }
        return map;

    }

    public static List<FreeFilter> asList(FreeFilter[] filters) {
        return Lists.newArrayList(filters);

    }

    /**
     * 收集指定对象的过滤条件。如果字段中有值，则作为过滤条件，否则忽略过滤条件。
     *
     * @param object 一个用 {@link FilterPart} 指定过滤条件字段的对象。
     * @return 返回一个 {@link List <FreeFilter>} 对象。
     */
    public static final List<FreeFilter> collect(Object object) {
        if (object == null) {
            return Lists.newArrayList();

        }

        List<FreeFilter> ffs = Lists.newArrayList();
        Field[] fields = object.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            if (f.isAnnotationPresent(FilterPart.class)) {
                FilterPart nf = f.getAnnotation(FilterPart.class);
                String name = StringUtils.isNotEmpty(nf.name())
                        ? nf.name()
                        : f.getName();

                String where = nf.where();

                Object val = value(object, nf, f);
                if (val == null)//如果值为空字符串,
                {
                    continue;
                }

                ffs.add(Filters.newFreeFilter(name, where).setValue(val));

            }

        }
        return ffs;

    }

    /**
     * 获取指定字段表示的过滤条件值。
     */
    private static Object value(Object object, FilterPart nf, Field f) {
        try {
            Object val = null;
            if (StringUtils.isNotEmpty(nf.filterValue())) {
                Object fv = Casting.to(nf.filterValueType()).tryParse(nf.filterValue());
                if (f.getBoolean(object)) {
                    val = fv;
                }

            } else {
                val = f.get(object);
            }

            if (val != null && val instanceof String && MatchPattern.None != nf.pattern()) {
                if (MatchPattern.Left.equals(nf.pattern())) {
                    val = val + "%";
                } else if (MatchPattern.Right.equals(nf.pattern())) {
                    val = "%" + val;
                } else if (MatchPattern.FullText.equals(nf.pattern())) {
                    val = "%" + val + "%";
                }

            }
            return val;

        } catch (Exception e) {
            return null;

        }

    }
}
