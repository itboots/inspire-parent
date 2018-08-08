/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util;

import com.google.common.base.Strings;

import javax.annotation.Nullable;

/**
 * @author LANXE
 */
public abstract class StringUtils extends org.springframework.util.StringUtils {

    /**
     * 表示空字符串
     */
    public static final String EMPTY = "";

    /**
     * Returns {@code true} if the given string is not null and  is not the empty string.
     *
     * @param string a string reference to check
     * @return {@code true} if the string is null or is the empty string
     */
    public static boolean isNotEmpty(@Nullable String string) {
        return !Strings.isNullOrEmpty(string);
    }

    public static boolean isNotEmpty(String... values) {
        boolean result = true;
        if (values != null && values.length != 0) {
            String[] array = values;
            int len = values.length;

            for (int i = 0; i < len; ++i) {
                String value = array[i];
                result &= !isEmpty(value);
            }
        } else {
            result = false;
        }

        return result;
    }
}
