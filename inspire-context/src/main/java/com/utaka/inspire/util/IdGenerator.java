/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util;

import com.google.common.base.Strings;

import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author XINEN
 */
public abstract class IdGenerator {

    private static AtomicLong uniqueId = new AtomicLong(System.nanoTime());

    /**
     * 根据当前时间生成的一个5位的字符串，使用A~Z,0~9 36个字符进行编码。
     */
    public static String get(int len) {
        if (len < 3) {
            len = 6;
        }
        Calendar now = Calendar.getInstance();

        if (len < 9) {
            return new StringBuilder()
                    .append(formatString36((
                            (now.get(Calendar.HOUR) * 3600
                                    + now.get(Calendar.MINUTE) * 60
                                    + now.get(Calendar.SECOND)) * 1000
                                    + now.get(Calendar.MILLISECOND)
                    ), len - 3))
                    .append(formatString36(uniqueId.incrementAndGet() % 10000, 3))
                    .toString();
        } else {
            return new StringBuilder()
                    .append(formatString36(now.get(Calendar.YEAR) % 1000, 2))
                    .append(formatString36(now.get(Calendar.MONTH), 1))
                    .append(formatString36(now.get(Calendar.DATE), 1))
                    .append(formatString36((
                            (now.get(Calendar.HOUR) * 3600
                                    + now.get(Calendar.MINUTE) * 60
                                    + now.get(Calendar.SECOND)) * 1000
                                    + now.get(Calendar.MILLISECOND)
                    ), len - 6))
                    .append(formatString36(uniqueId.incrementAndGet() % 10000, 3))
                    .toString();
        }
    }

    /**
     * 生成一个新的ID
     *
     * @return
     */
    public static String get() {
        String s = UUID.randomUUID().toString();
        return s.replace("-", "");
    }

    public static String formatString36(long val, int len) {
        long quotient = val;
        String result = "";
        while (quotient > 0) {
            long remainder = quotient % 36;
            if (remainder > 9) {
                result = (char) (remainder - 10 + 'A') + result;
            } else {
                result = remainder + result;
            }
            quotient /= 36;
        }
        result = Strings.padStart(result, len, '0');
        return result.substring(result.length() - len);
    }
}
