/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

/**
 *
 */
package com.utaka.inspire.context;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author XINEN
 */
public class BigDecimalTest {

    @Test
    public void testEquals() {
        BigDecimal bd1 = new BigDecimal("2.00");
        BigDecimal bd2 = new BigDecimal("2");
        assertThat(bd1)
                .isNotEqualTo(bd2)
                .isEqualByComparingTo(bd2);
    }
}
