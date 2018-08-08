/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util.crypto;

import java.security.NoSuchAlgorithmException;

/**
 * @author LANXE
 */
public interface KeyFunction {

    /**
     * 生成密钥
     *
     * @return 根据加密算法生成密钥
     */
    byte[] generateKey() throws NoSuchAlgorithmException;

}
