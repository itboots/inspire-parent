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
public class SelectToken extends JpqlToken implements Serializable {
    private static final long serialVersionUID = 1L;

    public SelectToken(String jpqlString) {
        super(jpqlString);
    }

}
