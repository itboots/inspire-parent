/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.domain;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * Query parameter helper to with the querys
 */
public class NamedParamBuilder {

    public static final Map<String, Object> EMPTY = new HashMap<String, Object>();

    private final Map<String, Object> parameters = Maps.newLinkedHashMap();

    private NamedParamBuilder() {

    }

    private NamedParamBuilder(String name, Object value) {
        this.parameters.put(name, value);
    }

    public static NamedParamBuilder newBuilder() {
        return new NamedParamBuilder();
    }

    public static NamedParamBuilder with(String name, Object value) {
        return new NamedParamBuilder(name, value);
    }

    public NamedParamBuilder and(String name, Object value) {
        this.parameters.put(name, value);
        return this;
    }

    public NamedParamBuilder and(Map<String, Object> params) {
        this.parameters.putAll(params);
        return this;
    }


    public Map<String, Object> build() {
        return this.parameters;
    }
}
