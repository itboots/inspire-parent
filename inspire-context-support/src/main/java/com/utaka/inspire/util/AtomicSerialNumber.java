/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util;

import com.google.common.base.Strings;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author lanxe
 */
public final class AtomicSerialNumber implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String prefix;

    private final long minValue;

    private final long maxValue;

    private final int minLength;

    private AtomicLong currentValue = new AtomicLong(0L);

    /**
     * @return the code
     */
    public String getPrefix() {
        return prefix;
    }

    public long getMinValue() {
        return minValue;
    }

    public long getMaxValue() {
        return maxValue;
    }

    public AtomicLong getCurrentValue() {
        return currentValue;
    }

    public AtomicSerialNumber(String prefix) {
        this.prefix = prefix;
        this.minValue = 0L;
        this.maxValue = Long.MAX_VALUE;
        this.minLength = 7;
    }

    public AtomicSerialNumber(String prefix, int minLength) {
        this.prefix = prefix;
        this.minValue = 0L;
        this.maxValue = Long.MAX_VALUE;
        this.minLength = minLength;
    }

    public AtomicSerialNumber(String prefix, Long maxValue) {
        this.prefix = prefix;
        this.minValue = 0L;
        this.maxValue = maxValue;
        this.minLength = 7;
    }


    public AtomicSerialNumber(String prefix, Long minValue, Long maxValue) {
        this.prefix = prefix;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.minLength = 7;
    }


    public String incrementAndGet() {
        long curVal = currentValue.get();
        while (curVal >= this.maxValue) {
            boolean updated = currentValue.compareAndSet(curVal, this.minValue);
            if (updated) {
                break;
            }

            curVal = currentValue.get();
        }

        return this.prefix + Strings.padStart(String.valueOf(this.currentValue.incrementAndGet()), minLength, '0');
    }
}
