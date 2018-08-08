/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util;

import com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @author XINEN
 */
public final class GZips {
    private static final int BUFFER = 2048;

    /**
     * 使用gzip进行压缩。
     *
     * @param source 要压缩的字符串
     * @throws IOException 压缩文件的过程中可能会抛出IO异常，请自行处理该异常。
     */
    public static String gzip(String source) throws IOException {
        if (Strings.isNullOrEmpty(source)) {
            return source;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(source.getBytes());
        } finally {
            if (gzip != null) {
                gzip.close();
            }
        }

        return new String(BaseEncoding.base64().encode(out.toByteArray()));
    }

    /**
     * 使用gzip进行解压缩
     *
     * @param source 要解压缩的字符串
     * @throws IOException 压缩文件的过程中可能会抛出IO异常，请自行处理该异常。
     */
    public static String gunzip(String source) throws IOException {
        if (source == null) {
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;
        GZIPInputStream ginzip = null;
        byte[] compressed = null;
        String decompressed = null;
        try {
            compressed = BaseEncoding.base64().decode(source);
            in = new ByteArrayInputStream(compressed);
            ginzip = new GZIPInputStream(in);

            byte[] buffer = new byte[BUFFER];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            decompressed = out.toString();
        } finally {
            if (ginzip != null) {
                ginzip.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }

        return decompressed;
    }

    /**
     * 压缩文件或者文件夹 压缩。
     *
     * @param source 要压缩的文件或者文件夹 建议使用"c:/abc"或者"c:/abc/aaa.txt"这种形式来给定压缩路径 使用"c:\\abc"
     *               或者"c:\\abc\\aaa.txt"这种形式来给定路径的话，可能导致出现压缩和解压缩路径意外故障。。。
     * @param target 压缩后的zip文件名称 压缩后的目录组织与windows的zip压缩的目录组织相同。 会根据压缩的目录的名称，在压缩文件夹中创建一个改名的根目录，
     *               其它压缩的文件和文件夹都在该目录下依照原来的文件目录组织形式
     * @throws IOException 压缩文件的过程中可能会抛出IO异常，请自行处理该异常。
     */
    public static File gzip(File source, File target)
            throws IOException {
        GZIPOutputStream gzip = null;
        FileInputStream in = null;
        try {
            gzip = new GZIPOutputStream(new FileOutputStream(target));
            in = new FileInputStream(source);
            byte[] bf = new byte[BUFFER];
            int pos = 0;
            while ((pos = in.read(bf)) != -1) {
                gzip.write(bf, 0, pos);
            }
        } finally {
            if (gzip != null) {
                gzip.close();
            }

            if (in != null) {
                gzip.close();
            }
        }

        return target;
    }

    /**
     * 使用gzip进行解压缩
     *
     * @param source 要解压缩的字符串
     * @throws IOException 压缩文件的过程中可能会抛出IO异常，请自行处理该异常。
     */
    public static File gunzip(File source, File target) throws IOException {
        OutputStream out = null;
        FileInputStream in = null;
        GZIPInputStream ginzip = null;
        try {
            in = new FileInputStream(source);
            out = new FileOutputStream(target);
            ginzip = new GZIPInputStream(in);

            byte[] buffer = new byte[BUFFER];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
        } finally {
            if (ginzip != null) {
                ginzip.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }

        return target;
    }
}
