/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jdbc;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author lanxe
 */
public class StaticDataSourceRouter extends AbstractRoutingDataSource {

    private volatile String current = "default";

    public void setCurrent(String current) {
        this.current = current;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return current;
    }
}
