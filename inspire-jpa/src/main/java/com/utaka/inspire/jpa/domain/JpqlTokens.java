/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.domain;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.utaka.inspire.jpa.annotation.*;
import com.utaka.inspire.util.StringUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.List;

/**
 */
public final class JpqlTokens {
    private static AccessibleObjectPredicate SELECT_COUNT_PREDICATE = new AccessibleObjectPredicate(SelectCountPart.class);
    private static AccessibleObjectPredicate SELECT_PREDICATE = new AccessibleObjectPredicate(SelectPart.class);
    private static AccessibleObjectPredicate FILTER_PREDICATE = new AccessibleObjectPredicate(FilterPart.class);
    private static AccessibleObjectPredicate GROUP_BY_PREDICATE = new AccessibleObjectPredicate(GroupByPart.class);
    private static AccessibleObjectPredicate HAVING_PREDICATE = new AccessibleObjectPredicate(HavingPart.class);
    private static AccessibleObjectPredicate ORDER_BY_PREDICATE = new AccessibleObjectPredicate(OrderByPart.class);

    private static List<Collector> collectors = Lists.newArrayList();

    private interface Collector {
        <T extends JpqlToken> Optional<T> collect(Field f, Object object);
    }

    static {
        collectors.add(new Collector() {
            @Override
            public <T extends JpqlToken> Optional<T> collect(Field f, Object object) {
                if (SELECT_COUNT_PREDICATE.apply(f)) {

                    SelectCountPart nf = f.getAnnotation(SelectCountPart.class);
                    String select = nf.value();

                    Object val = value(object, f);
                    if (val == null || (val instanceof Boolean && !((Boolean) val))) {
                        return Optional.absent();
                    }

                    return (Optional<T>) Optional.of(new SelectCountToken(select));

                }
                return Optional.absent();
            }
        });
        collectors.add(new Collector() {
            @Override
            public <T extends JpqlToken> Optional<T> collect(Field f, Object object) {
                if (SELECT_PREDICATE.apply(f)) {

                    SelectPart nf = f.getAnnotation(SelectPart.class);
                    String select = nf.value();

                    Object val = value(object, f);
                    if (val == null || (val instanceof Boolean && !((Boolean) val))) {
                        return Optional.absent();
                    }

                    return (Optional<T>) Optional.of(new SelectToken(select));

                }
                return Optional.absent();
            }
        });
        collectors.add(new Collector() {
            @Override
            public <T extends JpqlToken> Optional<T> collect(Field f, Object object) {
                if (FILTER_PREDICATE.apply(f)) {
                    FilterPart nf = f.getAnnotation(FilterPart.class);
                    String name = StringUtils.isNotEmpty(nf.name())
                            ? nf.name()
                            : f.getName();

                    String where = nf.where();

                    Object val = value(object, nf, f);
                    //如果值为空字符串,则作为常量过滤条件处理。
                    if (val == null)
                        return Optional.absent();

                    return (Optional<T>) Optional.of(new FreeFilter(name, where, val));

                }
                return Optional.absent();
            }
        });

        collectors.add(new Collector() {
            @Override
            public <T extends JpqlToken> Optional<T> collect(Field f, Object object) {
                if (GROUP_BY_PREDICATE.apply(f)) {

                    GroupByPart nf = f.getAnnotation(GroupByPart.class);
                    String group = nf.value();

                    Object val = value(object, f);
                    if (val == null || (val instanceof Boolean && !((Boolean) val))) {
                        return Optional.absent();
                    }

                    return (Optional<T>) Optional.of(new GroupByToken(group));

                }
                return Optional.absent();
            }
        });

        collectors.add(new Collector() {
            @Override
            public <T extends JpqlToken> Optional<T> collect(Field f, Object object) {
                if (HAVING_PREDICATE.apply(f)) {
                    HavingPart havingPart = f.getAnnotation(HavingPart.class);
                    String name = StringUtils.isNotEmpty(havingPart.name())
                            ? havingPart.name()
                            : f.getName();

                    String having = havingPart.having();

                    Object val = value(object, havingPart, f);
                    //如果值为空字符串,则作为常量过滤条件处理。
                    if (val == null) {
                        return Optional.absent();
                    }

                    return (Optional<T>) Optional.of(new HavingToken(name, having, val));

                }
                return Optional.absent();
            }
        });

        collectors.add(new Collector() {
            @Override
            public <T extends JpqlToken> Optional<T> collect(Field f, Object object) {
                if (ORDER_BY_PREDICATE.apply(f)) {
                    Object val = value(object, f);
                    if (val == null) {
                        return Optional.absent();
                    }
                    OrderByPart orderByPart = f.getAnnotation(OrderByPart.class);
                    String name = !ObjectUtils.isEmpty(val)
                            ? val.toString()
                            : f.getName();

                    return (Optional<T>) Optional.of(new OrderByToken(String.format("%s %s", name, orderByPart.direction().name())));

                }
                return Optional.absent();
            }
        });
    }

    /**
     * 收集指定对象的过滤条件。如果字段中有值，则作为过滤条件，否则忽略过滤条件。
     *
     * @param object 一个用 {@link FilterPart} 指定过滤条件字段的对象。
     * @return 返回一个 {@link List <JpqlToken>} 对象。
     */
    public static final List<JpqlToken> collect(Object object) {
        if (object == null) {
            return Lists.newArrayList();

        }

        List<JpqlToken> jpqlTokens = Lists.newArrayList();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field f : fields) {
            for (Collector collector : collectors) {
                Optional<JpqlToken> tokenOptional = collector.collect(f, object);
                if (tokenOptional.isPresent()) {
                    jpqlTokens.add(tokenOptional.get());
                }
            }
        }
        return jpqlTokens;

    }

    /**
     * 获取指定字段表示的过滤条件值。
     */
    static Object value(Object object, FilterPart filterPart, Field f) {
        try {
            Object val = null;
            if (StringUtils.isNotEmpty(filterPart.filterValue())) {
                Object fv = Casting.to(filterPart.filterValueType()).tryParse(filterPart.filterValue());
                if (f.getBoolean(object)) {
                    val = fv;
                }

            } else {
                val = f.get(object);
            }

            if (val != null && val instanceof String && FilterPart.MatchPattern.None != filterPart.pattern()) {
                if (FilterPart.MatchPattern.Left.equals(filterPart.pattern())) {
                    val = val + "%";
                } else if (FilterPart.MatchPattern.Right.equals(filterPart.pattern())) {
                    val = "%" + val;
                } else if (FilterPart.MatchPattern.FullText.equals(filterPart.pattern())) {
                    val = "%" + val + "%";
                }

            }
            return val;

        } catch (Exception e) {
            return null;

        }

    }

    /**
     * 获取指定字段表示的过滤条件值。
     */
    static Object value(Object object, HavingPart havingPart, Field f) {
        try {
            Object val = null;
            if (StringUtils.isNotEmpty(havingPart.filterValue())) {
                Object fv = Casting.to(havingPart.filterValueType()).tryParse(havingPart.filterValue());
                if (f.getBoolean(object)) {
                    val = fv;
                }

            } else {
                val = f.get(object);
            }
            return val;

        } catch (Exception e) {
            return null;

        }

    }

    static Object value(Object object, Field f) {
        try {
            return f.get(object);

        } catch (Exception e) {
            return null;

        }

    }

    private static class AccessibleObjectPredicate<T extends AccessibleObject> implements Predicate<T> {
        final Class<? extends Annotation> annotationClass;

        AccessibleObjectPredicate(Class<? extends Annotation> annotationClass) {
            this.annotationClass = annotationClass;
        }

        @Override
        public boolean apply(@Nonnull T input) {
            return input.isAnnotationPresent(annotationClass);
        }
    }
}
