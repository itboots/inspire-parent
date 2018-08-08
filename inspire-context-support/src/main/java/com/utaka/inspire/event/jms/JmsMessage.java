/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

/**
 *
 */
package com.utaka.inspire.event.jms;

import com.google.common.base.MoreObjects;

/**
 * @author lanxe
 */
public class JmsMessage {
    public static final String BODY_CLASS = "__body_class__";
    public static final String CONTENT_ENCODING = "Content-Encoding";
    public static final String TIMESTAMP = "_timestamp";
    public static final String CHANNEL = "_channel";
    public static final String SERIAL_NUMBER = "_serial_number";

    private String id;
    private String header;
    private String body;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this.getClass())
                .add("header", header)
                .add("body", this.body)
                .toString();
    }


}
