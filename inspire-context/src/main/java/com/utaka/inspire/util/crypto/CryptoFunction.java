/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util.crypto;

import java.security.GeneralSecurityException;

/**
 * @author lanxe
 */
public interface CryptoFunction extends KeyFunction, EncryptFunction {
    /**
     * 解密
     *
     * @param bytes 要解密的字节数组。
     * @param key   密钥
     * @return 返回揭秘后的字节数组。
     */
    byte[] decrypting(byte[] bytes, byte[] key) throws GeneralSecurityException;

    /**
     * 加密
     *
     * @param encrypted 要解密的字符串。
     * @param key       密钥
     * @return 返回解密后的字符串。
     */
    String decrypting(String encrypted, String key) throws GeneralSecurityException;

}
