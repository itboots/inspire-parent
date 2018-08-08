/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util;

import org.junit.Test;

import java.io.IOException;

/**
 */
public class GZipTests {

    @Test
    public void testgzip() throws IOException {
        StringBuilder sb = new StringBuilder();
        String uuid = "中国";
        for (int i = 0; i < 1000; i++) {
            sb.append(uuid);
        }

        System.out.println("olen:" + sb.length());
        String gzip = GZips.gzip(sb.toString());
        System.out.println("gzip:" + gzip.length());
        String s = GZips.gunzip(gzip);
        System.out.println("equals:" + sb.toString().equals(s));
    }

}
