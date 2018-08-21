/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.context;

/**
 * @author lanxe
 */
public final class StaticDataSourceAware implements DataSourceAware {
    private volatile String current = "default";

    public void setCurrent(String current) {
        this.current = current;
    }

    @Override
    public String getCurrentDataSource() {
        return current;
    }
}
