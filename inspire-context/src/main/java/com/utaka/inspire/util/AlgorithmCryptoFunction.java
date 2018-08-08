/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;
import com.utaka.inspire.util.crypto.CryptoFunction;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

final class AlgorithmCryptoFunction implements CryptoFunction {

    private final String algorithm;

    private final int keyLength;

    public AlgorithmCryptoFunction(String algorithm, int keyLength) {
        this.algorithm = algorithm;
        this.keyLength = keyLength;
    }


    @Override
    public byte[] generateKey() throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance(algorithm);
        kg.init(this.keyLength);
        return kg.generateKey().getEncoded();
    }

    @Override
    public byte[] encrypting(byte[] bytes, byte[] key) throws GeneralSecurityException {
        //生成Cipher对象,指定其支持的DES算法
        Cipher cipher = Cipher.getInstance(algorithm);
        // 根据密钥，对Cipher对象进行初始化，ENCRYPT_MODE表示加密模式
        cipher.init(Cipher.ENCRYPT_MODE, toKey(key));
        return cipher.doFinal(bytes);
    }

    @Override
    public String encrypting(String original, String key) throws GeneralSecurityException {
        byte[] bytes = encrypting(original.getBytes(Charsets.UTF_8), keyStringToBytes(key));
        return BaseEncoding.base64().encode(bytes);
    }

    @Override
    public byte[] decrypting(byte[] bytes, byte[] key) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, toKey(key));
        return cipher.doFinal(bytes);
    }

    @Override
    public String decrypting(String encrypted, String key) throws GeneralSecurityException {
        byte[] encryptedBytes = BaseEncoding.base64().decode(encrypted);
        byte[] bytes = decrypting(encryptedBytes, keyStringToBytes(key));
        return new String(bytes, Charsets.UTF_8);
    }

    /**
     * 转换密钥
     *
     * @param key 二进制密钥
     * @return Key  密钥
     * @throws Exception
     */
    protected Key toKey(byte[] key) {
        return new SecretKeySpec(key, algorithm);
    }

    private byte[] keyStringToBytes(String key) {
        return getKey(key.getBytes(Charsets.UTF_8));
    }

    private byte[] getKey(byte[] originalBytes) {
        int size = this.keyLength / 8;
        return Arrays.copyOf(originalBytes, size < 8 ? 8 : size);
    }
}
