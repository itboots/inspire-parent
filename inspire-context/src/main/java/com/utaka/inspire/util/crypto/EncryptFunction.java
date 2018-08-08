/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util.crypto;

import java.security.GeneralSecurityException;

/**
 * @author LANXE
 */
public interface EncryptFunction {

    /**
     * 加密
     *
     * @param bytes 要加密的字节数组。
     * @param key   密钥
     * @return 返回加密后的字节数组。
     */
    byte[] encrypting(byte[] bytes, byte[] key) throws GeneralSecurityException;

    /**
     * 加密
     *
     * @param original 要加密的字节数组。
     * @param key      密钥
     * @return 返回加密后的字符串。
     */
    String encrypting(String original, String key) throws GeneralSecurityException;

}
