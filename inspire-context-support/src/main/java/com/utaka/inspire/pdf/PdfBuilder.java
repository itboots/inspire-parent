/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * @author XINEN
 */
public final class PdfBuilder {

    private final Document doc = new Document();
    private PdfWriter writer;
    private Font font;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    public InputStream build() {
        if (doc.isOpen()) {
            doc.close();
        }
        return new ByteArrayInputStream(output.toByteArray());
    }

    public PdfBuilder addTitle(String title) {
        doc().addTitle(title);
        return this;
    }

    public PdfBuilder addSubject(String subject) {
        doc().addSubject(subject);
        return this;
    }

    public PdfBuilder addAuthor(String author) {
        doc().addAuthor(author);
        return this;
    }

    public PdfBuilder addCreator(String creator) {
        doc().addCreator(creator);
        return this;

    }

    public PdfBuilder addProducer() {
        doc().addProducer();
        return this;
    }

    public PdfBuilder addCreationDate() {
        doc().addCreationDate();
        return this;

    }

    public PdfBuilder addHeader(String name, String content) {
        doc().addHeader(name, content);
        return this;
    }

    public PdfBuilder text(String content) {
        return add(new Paragraph(content, this.font));

    }

    public PdfBuilder image(String filename) throws MalformedURLException,
            IOException, DocumentException {
        Image image = Image.getInstance(filename);
        image.setAlignment(Image.MIDDLE);
        return image(image);

    }

    public PdfBuilder image(String filename, int alignment) throws MalformedURLException,
            IOException, DocumentException {
        Image image = Image.getInstance(filename);
        image.setAlignment(alignment);
        return image(image);

    }

    public PdfBuilder image(byte[] bytes) throws MalformedURLException,
            IOException, DocumentException {
        Image image = Image.getInstance(bytes);
        image.setAlignment(Image.MIDDLE);
        return image(image);

    }

    public PdfBuilder image(byte[] bytes, int alignment) throws MalformedURLException,
            IOException, DocumentException {
        Image image = Image.getInstance(bytes);
        image.setAlignment(alignment);
        return image(image);

    }

    public PdfBuilder image(ByteArrayOutputStream stream) throws MalformedURLException,
            IOException, DocumentException {
        stream.flush();
        return image(stream.toByteArray());

    }

    public PdfBuilder image(ByteArrayOutputStream stream, int alignment)
            throws MalformedURLException,
            IOException, DocumentException {
        stream.flush();
        return image(stream.toByteArray(), alignment);

    }

    public PdfBuilder image(Image image) throws MalformedURLException,
            IOException, DocumentException {
        return add(image);

    }

    public PdfBuilder newPage() {
        doc().newPage();
        return this;
    }

    public PdfBuilder newPage(String content) {
        doc().newPage();
        return this.text(content);
    }

    private PdfBuilder() {
        try {
            writer = PdfWriter.getInstance(doc, output);
            BaseFont baseFont = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
            font = new Font(baseFont);

        } catch (DocumentException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Document doc() {
        if (!doc.isOpen()) {
            doc.open();
        }
        return doc;
    }

    private PdfBuilder add(Element element) {
        try {
            doc().add(element);

        } catch (DocumentException e) {
            e.printStackTrace();

        }
        return this;

    }

    public static final PdfBuilder newBuilder() {
        return new PdfBuilder();
    }

}
