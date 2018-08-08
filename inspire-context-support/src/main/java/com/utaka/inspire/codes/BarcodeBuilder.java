/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.codes;

import com.google.common.collect.Maps;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author XINEN
 */
public final class BarcodeBuilder {

    private final Map<EncodeHintType, Object> hints;
    private int width = 400;
    private int height = 400;
    private BarcodeFormat format = BarcodeFormat.QR_CODE;
    private BitMatrix bitMatrix;
    private String remark;

    private BarcodeBuilder() {
        this.hints = Maps.newHashMap();
        this.hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
    }

    private BarcodeBuilder(Map<EncodeHintType, Object> hints) {
        this.hints = checkNotNull(hints);
    }


    public BarcodeBuilder encoding(String charset) {
        hints.put(EncodeHintType.CHARACTER_SET, charset);
        return this;
    }

    public BarcodeBuilder size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public BarcodeBuilder format(BarcodeFormat format) {
        this.format = format;
        return this;
    }

    public BarcodeBuilder encode(String content) throws WriterException {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        bitMatrix = multiFormatWriter.encode(content, this.format, this.width, this.height, hints);
        return this;
    }

    public BarcodeBuilder encode(String content, BarcodeFormat format) throws WriterException {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        bitMatrix = multiFormatWriter.encode(content, format, this.width, this.height, hints);
        return this;

    }

    public BarcodeBuilder remark(String remark) {
        this.remark = remark;
        return this;
    }

    public BufferedImage toBufferedImage() {
        checkNotNull(bitMatrix, "call encode before");
        return MatrixToImageWriter.toBufferedImage(bitMatrix, remark);

    }

    public void writeToFile(String format, File file) throws IOException {
        checkNotNull(bitMatrix, "call encode before");
        MatrixToImageWriter.writeToFile(bitMatrix, remark, format, file);

    }

    public void writeToStream(String format, OutputStream stream) throws IOException {
        checkNotNull(bitMatrix, "call encode before");
        MatrixToImageWriter.writeToStream(bitMatrix, remark, format, stream);
    }

    public static BarcodeBuilder newBuilder() {
        return new BarcodeBuilder();
    }

    public static BarcodeBuilder newBuilder(Map<EncodeHintType, Object> hints) {
        return new BarcodeBuilder(hints);
    }

}
