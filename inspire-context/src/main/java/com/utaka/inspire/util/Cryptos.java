/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util;

import com.google.common.annotations.Beta;
import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;
import com.utaka.inspire.util.crypto.CryptoFunction;
import com.utaka.inspire.util.crypto.EncryptFunction;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;

/**
 * @author LANXE
 */
@Beta
public final class Cryptos {

    public static final CryptoFunction DES = AlgorithmHolder.DES;

    public static final CryptoFunction DESede = AlgorithmHolder.DESEede;

    public static final CryptoFunction AES = AlgorithmHolder.AES;

    public static final EncryptFunction HmacMD5 = new AlgorithmMacFunction("HmacMD5");

    public static final EncryptFunction HmacSHA1 = new AlgorithmMacFunction("HmacSHA1");

    private static class AlgorithmHolder {
        static final CryptoFunction DES = new AlgorithmCryptoFunction("DES", 56);

        static final CryptoFunction DESEede = new AlgorithmCryptoFunction("DESede", 168);

        static final CryptoFunction AES = new AlgorithmCryptoFunction("AES", 128);
    }

    private static class AlgorithmMacFunction implements EncryptFunction {

        private final String algorithm;

        public AlgorithmMacFunction(String algorithm) {
            this.algorithm = algorithm;
        }


        @Override
        public byte[] encrypting(byte[] bytes, byte[] key) throws GeneralSecurityException {
            SecretKey secretKey = new SecretKeySpec(key, algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(secretKey);
            return mac.doFinal(bytes);
        }

        @Override
        public String encrypting(String original, String key) throws GeneralSecurityException {
            byte[] bytes = encrypting(original.getBytes(Charsets.UTF_8), key.getBytes(Charsets.UTF_8));
            return BaseEncoding.base64().encode(bytes);
        }
    }
}
