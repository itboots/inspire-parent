
/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class SerializingTests {

    @Test
    public void test1() {

        Object o = DateTime.now();
        String s = Serializing.json().toString(o);
        System.out.println(s);

        System.out.println(Serializing.json().toObject(s, DateTime.class));


    }

    @Test
    public void test2() {
        DateTimeFormatter format = DateTimeFormat.forPattern(Serializing.JsonHolder.DEFAULT_DATE_TIME_FORMAT);

        System.out.println(format.parseDateTime("2015-04-01 13:27:10"));
    }
}
