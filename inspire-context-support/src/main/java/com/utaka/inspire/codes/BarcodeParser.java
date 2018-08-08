/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.codes;

import com.google.common.collect.Maps;
import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author XINEN
 */
public class BarcodeParser {

    private final Map<DecodeHintType, Object> hints = Maps.newHashMap();

    private BarcodeParser() {
        this.encoding("UTF-8");
    }

    public BarcodeParser encoding(String charset) {
        hints.put(DecodeHintType.CHARACTER_SET, charset);
        return this;
    }

    public Result parse(File file) throws IOException, NotFoundException {
        MultiFormatReader formatReader = new MultiFormatReader();
        BufferedImage image = ImageIO.read(file);
        ;
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

        return formatReader.decode(binaryBitmap, hints);

    }

}
