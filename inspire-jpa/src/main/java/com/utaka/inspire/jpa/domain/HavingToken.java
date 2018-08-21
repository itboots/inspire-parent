/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.domain;

import java.io.Serializable;

/**
 * 定义{JPQL}的一部分.用于构造JPQL语句.
 *
 * @author XINEN
 */
public class HavingToken extends ParamToken implements Serializable {
    private static final long serialVersionUID = 1L;

    public HavingToken(String having) {
        super(having);
    }

    public HavingToken(String name, String having) {
        super(name, having);
    }

    public HavingToken(String name, String having, Object value) {
        super(name, having, value);
    }
}
