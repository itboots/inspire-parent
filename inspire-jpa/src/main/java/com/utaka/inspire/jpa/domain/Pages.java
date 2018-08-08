/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.domain;

import com.google.common.collect.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author XINEN
 */
public final class Pages {

    public static <T> Page<T> newPage() {
        List<T> result = Lists.newArrayList();
        return new PageImpl<T>(result);

    }

    public static <T> Page<T> newPage(List<T> content) {
        return new PageImpl<T>(content);

    }

    public static <T> Page<T> newPage(List<T> content, Pageable pageable, long total) {
        return new PageImpl<T>(content, pageable, total);
    }
}
