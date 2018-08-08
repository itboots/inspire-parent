
/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;
import com.utaka.inspire.util.crypto.CryptoFunction;
import org.junit.Before;
import org.junit.Test;

import java.security.GeneralSecurityException;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

/**
 */
public class CryptoTest {

    private static final String CMB_PAY_BRANCHID = "cmb.pay.branchid";

    private static final String CMB_PAY_CONO = "cmb.pay.cono";

    private static final String CMB_PAY_PASSWORD = "cmb.pay.password";

    private static final String CMB_PAY_KEY = "cmb.pay.key";

    private static final String CMB_PAY_MERCHANT_URL = "cmb.pay.menchant.url";

    private static final String CMB_PAY_MERCHANT_PARA = "cmb.pay.menchant.para";

    private static final String CMB_PAY_PUBLISH_KEY = "cmb.pay.publickey";

    private static final String CMB_PAID_REDIRECT_URL = "cmb.paid.redirect.url";

    private static final String CMB_SETTLE_OPTIONS = "cmb.settle.options";

    private Properties props;

    @Before
    public void setup() {
        this.props = new Properties();
        props.setProperty(CMB_PAY_BRANCHID, "0755");
        props.setProperty(CMB_PAY_CONO, "100954");
        props.setProperty(CMB_PAY_PASSWORD, "654321");
        props.setProperty(CMB_PAY_KEY, "Sunisco123456789");
        props.setProperty(CMB_PAY_MERCHANT_URL, "/api/cmb/receive");
        props.setProperty(CMB_PAY_MERCHANT_PARA, "");
        props.setProperty(CMB_PAY_PUBLISH_KEY, "/META-INF/public.key");
        props.setProperty(CMB_PAID_REDIRECT_URL, "/eir/trade");
        props.setProperty(CMB_SETTLE_OPTIONS, "payment.ebank.cmbchina.com");
    }

    @Test
    public void testAES() throws GeneralSecurityException {
        test(Cryptos.AES);

    }

    @Test
    public void testDES() throws GeneralSecurityException {
        test(Cryptos.DES);
        testString(Cryptos.DES);
    }

    @Test
    public void testDESede() throws GeneralSecurityException {
        test(Cryptos.DESede);
    }

    @Test
    public void testHmacMD5() throws GeneralSecurityException {
        String str = Serializing.json().toString(props);
        byte[] bytes = Cryptos.HmacMD5.encrypting(str.getBytes(Charsets.UTF_8), "test".getBytes(Charsets.UTF_8));

    }


    private void test(CryptoFunction function) throws GeneralSecurityException {
        String str = Serializing.json().toString(props);
        byte[] key = function.generateKey();
        System.out.println("key:" + BaseEncoding.base64().encode(key));
        System.out.println("key:" + key.length);
        byte[] bytes = function.encrypting(str.getBytes(Charsets.UTF_8), key);
        String encrypted = BaseEncoding.base64().encode(bytes);

        assertThat(encrypted)
                .isNotEmpty();

        bytes = function.decrypting(bytes, key);
        String actual = new String(bytes, Charsets.UTF_8);
        assertThat(actual)
                .isNotEmpty()
                .isEqualTo(str)
        ;

        Properties actualProps = Serializing.json().toObject(actual, Properties.class);

        assertThat(actualProps)
                .isNotEmpty()
                .isInstanceOf(Properties.class)
                .containsAllEntriesOf(props)
        ;
    }

    private void testString(CryptoFunction function) throws GeneralSecurityException {
        String str = Serializing.json().toString(props);
        String key = "12345678";
        String encrypted = function.encrypting(str, key);

        assertThat(encrypted)
                .isNotEmpty();

        String actual = function.decrypting(encrypted, key);
        assertThat(actual)
                .isNotEmpty()
                .isEqualTo(str)
        ;

        Properties actualProps = Serializing.json().toObject(actual, Properties.class);

        assertThat(actualProps)
                .isNotEmpty()
                .isInstanceOf(Properties.class)
                .containsAllEntriesOf(props)
        ;
    }
}
