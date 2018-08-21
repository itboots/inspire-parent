/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa;

import com.utaka.inspire.jpa.domain.JpqlQuery;
import com.utaka.inspire.jpa.domain.JpqlToken;
import com.utaka.inspire.jpa.domain.OrderCriterias;
import com.utaka.inspire.jpa.util.JpqlUtils;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * Created by lanxe on 2016/10/19.
 */
public class TokenTests {
    @Test
    public void testCriteria() {
        OrderCriterias orderCriterias = new OrderCriterias();
        orderCriterias.name = "test";
        orderCriterias.having = new Date();
        List<JpqlToken> tokens = orderCriterias.tokens();
        JpqlQuery query = JpqlUtils.createQuery(tokens);
        System.out.println(query.toString());

        query = JpqlUtils.createCountQuery(tokens);
        System.out.println(query.toString());
    }
}
