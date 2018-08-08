/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.codes;

import com.google.zxing.common.BitMatrix;
import com.utaka.inspire.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author XINEN
 */
final class MatrixToImageWriter {

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    private MatrixToImageWriter() {
    }

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        return toBufferedImage(matrix, null);
    }

    public static BufferedImage toBufferedImage(BitMatrix matrix, String remark) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }

        if (StringUtils.isNotEmpty(remark)) {
            wirteRemark(image, remark);
        }

        return image;
    }

    public static void writeToFile(BitMatrix matrix, String format, File file)
            throws IOException {
        writeToFile(matrix, null, format, file);
    }

    public static void writeToFile(BitMatrix matrix, String remark, String format, File file)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix, remark);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format " + format + " to " + file);
        }
    }

    public static void writeToStream(BitMatrix matrix, String format, OutputStream stream)
            throws IOException {
        writeToStream(matrix, null, format, stream);
    }

    public static void writeToStream(BitMatrix matrix, String remark, String format,
                                     OutputStream stream)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix, remark);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    private static void wirteRemark(BufferedImage image, String remark) {
        Graphics2D g = image.createGraphics();
        int width = image.getWidth();
        int height = image.getHeight();
        g.setColor(Color.WHITE);
        g.fillRect(0, height - 10, width, 10);

        int x = (width - remark.length() * 8) / 2;
        int y = height;

        // 绘制备注信息
        g.setColor(Color.BLACK);
        g.drawString(remark, x, y);

        g.dispose();
    }

}