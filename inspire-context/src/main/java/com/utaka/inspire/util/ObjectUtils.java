/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util;

import org.springframework.lang.Nullable;

/**
 * @author lanxe
 */
public abstract class ObjectUtils extends org.springframework.util.ObjectUtils {
    public static boolean isNotEmpty(@Nullable Object obj) {
        return !isEmpty(obj);
    }
}
