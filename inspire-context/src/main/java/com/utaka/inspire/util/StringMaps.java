/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util;

import com.google.common.base.*;
import com.google.common.collect.Ordering;
import com.google.common.io.BaseEncoding;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Map;

/**
 * @author LANXE
 */
public final class StringMaps {
    private StringMaps() {
    }

    private enum EntryFunction implements Function<Map.Entry<String, ?>, Object> {
        KEY {
            @Override
            @Nullable
            public Object apply(Map.Entry<String, ?> entry) {
                return entry.getKey();
            }
        },
        VALUE {
            @Override
            @Nullable
            public Object apply(Map.Entry<String, ?> entry) {
                return entry.getValue();
            }
        };
    }

    @SuppressWarnings("unchecked")
    static Function<Map.Entry<String, ?>, String> keyFunction() {
        return (Function) EntryFunction.KEY;
    }


    public static Ordering<Map.Entry<String, ?>> onKeys() {
        return Ordering.natural().onResultOf(StringMaps.keyFunction());
    }

    /**
     * 从给定的{@link Map}中取非空的值。
     *
     * @param key 要从{@link Map}中取值的键。
     * @return 如果指定的键为 null 或者{@link Map}不包含该键，则返回空字符串；否则从map中取值。
     */
    public static String nullToEmpty(String key, Map<String, String> stringMap) {
        return (Strings.isNullOrEmpty(key) || !stringMap.containsKey(key))
                ? ""
                : stringMap.get(key);
    }

    public static Converter<String, Map<String, String>> stringConverter() {
        return StringConverter.INSTANCE;
    }

    private static class StringConverter extends Converter<String, Map<String, String>> implements Serializable {
        private static final long serialVersionUID = 1;
        private static final Splitter.MapSplitter SPLITTER = Splitter.on("&").withKeyValueSeparator("=");
        private static final Joiner.MapJoiner JOINER = Joiner.on("&").withKeyValueSeparator("=").useForNull("");

        static final StringConverter INSTANCE = new StringConverter();

        @Override
        protected Map<String, String> doForward(String s) {
            return SPLITTER.split(s);
        }

        @Override
        protected String doBackward(Map<String, String> map) {
            return JOINER.join(map);
        }

        @Override
        public String toString() {
            return "StringMaps.stringConverter()";
        }

    }

    public static Converter<String, Map<String, String>> base64Converter() {
        return Base64Converter.INSTANCE;
    }

    private static class Base64Converter extends StringConverter implements Serializable {
        private static final long serialVersionUID = 1;
        static final BaseEncoding BASE64_URL = BaseEncoding.base64Url().withPadChar('&');
        static final Base64Converter INSTANCE = new Base64Converter();

        @Override
        protected Map<String, String> doForward(String s) {
            return super.doForward(new String(BASE64_URL.decode(s), Charsets.UTF_8));
        }

        @Override
        protected String doBackward(Map<String, String> map) {
            return BASE64_URL.encode(super.doBackward(map).getBytes(Charsets.UTF_8));
        }

        @Override
        public String toString() {
            return "StringMaps.base64Converter()";
        }

    }
}
