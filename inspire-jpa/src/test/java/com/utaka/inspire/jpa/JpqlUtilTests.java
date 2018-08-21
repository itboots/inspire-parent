/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.utaka.inspire.jpa.domain.JpqlQuery;
import com.utaka.inspire.jpa.util.JpqlUtils;
import org.junit.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;

import java.util.Set;

/**
 */
public class JpqlUtilTests {

    @Test
    public void test() {
        String queryString = "SELECT DISTINCT ord FROM OrderDetailRef as ord LEFT JOIN FETCH ord.product AS pro LEFT JOIN FETCH ord.lines lines";

        queryString = JpqlUtils.applySorting(queryString, new Sort(Sort.Direction.DESC, "pro.categoryCode"));

        System.out.println(queryString);

    }

    @Test
    public void testAlias() {
        String queryString = "SELECT DISTINCT ord FROM OrderDetailRef as ord LEFT JOIN FETCH ord.product AS pro LEFT JOIN FETCH ord.lines lines";

        String query = QueryUtils.detectAlias(queryString);

        System.out.println(query);
        query = QueryUtils.createCountQueryFor(queryString);

        System.out.println(query);

        query = JpqlUtils.createPrimaryKeyQueryFor(queryString, "id");

        System.out.println(query);
    }

    @Test
    public void testCreatePrimaryKeyQueryFor() {
        String queryString = "SELECT DISTINCT ord FROM OrderDetailRef as ord LEFT JOIN FETCH ord.product AS pro LEFT JOIN FETCH ord.lines lines WHERE id in (select id from where aaa in :cntno) and no in (select id from where aaa in :cntno)";
        Set<String> id = Sets.newHashSet("id1");
        JpqlQuery query = JpqlUtils.createQueryByPrimaryKey(queryString, Maps.<String, Object>newHashMap(), id);

        System.out.println(query);
    }
}
