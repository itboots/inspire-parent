/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util;


import com.google.common.collect.Iterables;

import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author LANXE
 */
public abstract class ObjectUtils extends org.springframework.util.ObjectUtils {
    public static boolean isNotEmpty(@Nullable Object obj) {
        return !isEmpty(obj);
    }

    public static boolean isEmpty(@Nullable Object obj) {
        if (obj == null) {
            return true;
        }

        if (obj instanceof Optional) {
            return !((Optional) obj).isPresent();
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Iterable) {
            return Iterables.isEmpty((Iterable) obj);
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        // else
        return Objects.isNull(obj);
    }
}
