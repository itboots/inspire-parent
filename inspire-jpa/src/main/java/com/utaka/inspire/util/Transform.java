/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.hibernate.mapping.Collection;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author XINEN
 */
public final class Transform extends org.springframework.beans.BeanUtils {

    private static final Map<Class<?>, Map<Class<?>, Copyable<?, ?>>> TRANSFERS =
            Maps.newConcurrentMap();

    public static synchronized <TSource, TTarget> Copyable<TSource, TTarget> register(
            Class<TSource> sourceType,
            Class<TTarget> targetType,
            Copyable<TSource, TTarget> transfer) {

        if (sourceType != null && targetType != null) {
            if (!TRANSFERS.containsKey(sourceType)) {
                Map<Class<?>, Copyable<?, ?>> map = Maps.newConcurrentMap();
                TRANSFERS.put(sourceType, map);
            }
            TRANSFERS.get(sourceType).put(targetType, transfer);
        }

        return transfer;

    }


    @SuppressWarnings("unchecked")
    public static <TSource, TTarget> TTarget transfor(TSource source, TTarget target) {
        if (source == null) {
            return null;
        }

        checkNotNull(target, "target can not be null!");

        Copyable<TSource, TTarget> copyfunction =
                (Copyable<TSource, TTarget>) getCopyFuncation(source.getClass(), target.getClass());

        if (copyfunction != null) {
            return copyfunction.copy(source, target);

        } else {
            return copySimpleProperty(source, target);
        }
    }

    public static <TSource, TTarget> Set<TTarget> copyList(
            Iterable<TSource> source,
            Class<TTarget> targetClazz) {
        Set<TTarget> sets = Sets.newHashSet();
        if (ObjectUtils.isNotEmpty(source)) {
            for (TSource s : source) {
                sets.add(copySimpleProperty(s, targetClazz));
            }
        }
        return sets;

    }

    public static <TSource, TTarget> TTarget copySimpleProperty(
            TSource source,
            Class<TTarget> targetClazz) {
        TTarget target = instantiate(targetClazz);
        return Transform.copySimpleProperty(source, target);

    }


    /**
     * Copy the property values of the given source bean into the given target bean.
     * <p>
     * Note: The source and target classes do not have to match or even be derived from each other,
     * as long as the properties match. Any bean properties that the source bean exposes but the
     * target bean does not will silently be ignored.
     *
     * @param source the source bean
     * @param target the target bean
     * @throws BeansException if the copying failed
     * @see BeanWrapper
     */

    public static <TSource, TTarget> TTarget copySimpleProperty(
            TSource source,
            TTarget target) {
        return copySimpleProperty(source, target, (Copyable<TSource, TTarget>) null);

    }

    public static <TSource, TTarget> TTarget copySimpleProperty(
            TSource source,
            TTarget target,
            Copyable<TSource, TTarget> childCopy) {
        return copySimpleProperty(source, target, false, childCopy);
    }

    /**
     * Copy the property values of the given source bean into the given target bean.
     * <p>
     * Note: The source and target classes do not have to match or even be derived from each other,
     * as long as the properties match. Any bean properties that the source bean exposes but the
     * target bean does not will silently be ignored.
     *
     * @param source the source bean
     * @param target the target bean
     * @see BeanWrapper
     */
    public static <TSource, TTarget> TTarget copySimpleProperty(
            TSource source,
            TTarget target,
            boolean ignoreNullValue,
            Copyable<TSource, TTarget> childCopy) {

        return copySimpleProperty(source, target, ignoreNullValue, childCopy, null);

    }

    public static <TSource, TTarget> TTarget copySimpleProperty(
            TSource source,
            TTarget target,
            boolean ignoreNullValue,
            Copyable<TSource, TTarget> childCopy1,
            Copyable<TSource, TTarget> childCopy2) {

        return copySimpleProperty(
                source, target, ignoreNullValue, childCopy1, childCopy2, null);

    }

    @SuppressWarnings("unchecked")
    public static <TSource, TTarget> TTarget copySimpleProperty(
            TSource source,
            TTarget target,
            boolean ignoreNullValue,
            Copyable<TSource, TTarget> childCopy1,
            Copyable<TSource, TTarget> childCopy2,
            Copyable<TSource, TTarget> childCopy3) {

        return copySimpleProperty(
                source,
                target,
                ignoreNullValue,
                Lists.newArrayList(childCopy1, childCopy2, childCopy3));

    }

    public static <TSource, TTarget> TTarget copySimpleProperty(
            TSource source,
            TTarget target,
            boolean ignoreNullValue,
            Iterable<Copyable<TSource, TTarget>> childrenCopies) {

        if (ObjectUtils.isEmpty(source) || ObjectUtils.isEmpty(target)) {
            return null;

        }

        target = copy(source, target, ignoreNullValue, true);
        if (childrenCopies != null && !Iterables.isEmpty(childrenCopies)) {
            for (Copyable<TSource, TTarget> c : childrenCopies) {
                target = c == null ? target : c.copy(source, target);
            }
        }
        return target;
    }

    public static <TSource, TTarget> TTarget copy(
            TSource source,
            TTarget target) {
        return copy(source, target, false, false);
    }

    public static <TSource, TTarget> TTarget copy(
            TSource source,
            TTarget target,
            boolean ignoreNullValue) {
        return copy(source, target, ignoreNullValue, false);
    }

    /**
     * Copy the property values of the given source bean into the given target bean.
     * <p>
     * Note: The source and target classes do not have to match or even be derived from each other,
     * as long as the properties match. Any bean properties that the source bean exposes but the
     * target bean does not will silently be ignored.
     *
     * @param source          the source bean
     * @param target          the target bean
     * @param ignoreNullValue if ignore the null property value
     * @throws BeansException if the copying failed
     * @see BeanWrapper
     */
    public static <TSource, TTarget> TTarget copy(
            TSource source,
            TTarget target,
            boolean ignoreNullValue,
            boolean ignoreCollectionProperty) {

        checkNotNull(source, "Source can not be null!");
        checkNotNull(target, "Target can not be null!");

        Class<?> actualEditable = target.getClass();

        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);

        for (PropertyDescriptor targetPd : targetPds) {
            if (targetPd.getWriteMethod() != null) {
                PropertyDescriptor sourcePd =
                        getPropertyDescriptor(source.getClass(), targetPd.getName());

                if (sourcePd != null && sourcePd.getReadMethod() != null) {
                    if (ignoreCollectionProperty
                            && Collection.class.isAssignableFrom(sourcePd.getPropertyType())) {
                        continue;
                    }

                    if (!sourcePd.getPropertyType().isAssignableFrom(targetPd.getPropertyType())) {
                        continue;
                    }

                    try {
                        Method readMethod = sourcePd.getReadMethod();
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source);
                        if (value == null && ignoreNullValue) {
                            continue;
                        }

                        readMethod = targetPd.getReadMethod();

                        if (Objects.equal(value, readMethod.invoke(target))) {
                            continue;
                        }

                        Method writeMethod = targetPd.getWriteMethod();
                        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                            writeMethod.setAccessible(true);
                        }
                        writeMethod.invoke(target, value);
                    } catch (Throwable ex) {
                        throw new FatalBeanException(
                                "Could not copy properties from source to target", ex);
                    }
                }
            }
        }

        return target;
    }

    @SuppressWarnings({"unchecked"})
    private static <TSource, TTarget> Copyable<TSource, TTarget> getCopyFuncation(
            Class<TSource> sourceType, Class<TTarget> targetType) {

        Map<Class<?>, Copyable<?, ?>> targetTransfers = null;

        Class<?> key = sourceType;
        while (key != null && !Object.class.equals(key)) {
            if (TRANSFERS.containsKey(key)) {
                targetTransfers = TRANSFERS.get(key);
                break;
            } else {
                key = key.getSuperclass();
            }
        }

        if (targetType != null && targetTransfers != null) {
            for (Class<?> tt : targetTransfers.keySet()) {
                if (tt.isAssignableFrom(targetType)) {
                    return (Copyable<TSource, TTarget>) targetTransfers.get(tt);
                }
            }

        }
        return null;
    }

    public interface Copyable<TSource, TTarget> {

        TTarget copy(TSource source, TTarget target);

    }

    public interface Transferable<TSource, TTarget> {

        TTarget transfor(TSource source);

    }
}
