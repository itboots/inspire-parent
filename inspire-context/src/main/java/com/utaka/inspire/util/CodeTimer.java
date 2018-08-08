/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util;

import org.springframework.util.StringUtils;

/**
 * @author lanxe
 */
public final class CodeTimer implements Disposable {

    private String text;
    private long startTime;
    private long freeMemory;

    private CodeTimer(boolean startFresh, String text) {
        if (true == startFresh) {
            prepareForOperation();
        }

        Runtime rt = Runtime.getRuntime();
        this.freeMemory = rt.freeMemory();
        this.text = text;

        // Get the time before returning so that any code above doesn't impact the time.
        startTime = System.nanoTime();

    }

    public static void time(String text, int numIterations, TimedOp op) {
        time(false, text, numIterations, op);

    }

    public static void time(boolean startFresh, String text, int numIterations, TimedOp op) {
        CodeTimer codeTime = new CodeTimer(startFresh, text);
        try {
            while (numIterations-- > 0) {
                op.apply();
            }

        } finally {
            codeTime.dispose();
        }

    }

    public static Disposable time(Boolean startFresh, String text) {
        return (new CodeTimer(startFresh, text));

    }

    public static Disposable time(String text) {
        return (time(false, text));
    }

    @Override
    public void dispose() {

        // Get the elapsed time now so that any code below doesn't impact the time.
        long elapsedTime = System.nanoTime() - startTime;

        if (false == StringUtils.isEmpty(text)) {
            System.out.printf("   %s", text);
            System.out.println();
            System.out.printf("   %d ms (freeMemory=%sKB)",
                    elapsedTime / 1000000,
                    (freeMemory - Runtime.getRuntime().freeMemory()) / 1024);
            System.out.println();
        }
    }

    private static void prepareForOperation() {
        System.gc();
        System.runFinalization();
        System.gc();
    }

    public interface TimedOp {
        void apply();

    }

}
