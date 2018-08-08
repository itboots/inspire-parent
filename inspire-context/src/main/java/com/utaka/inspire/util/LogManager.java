/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author LANXE
 */
public class LogManager {

    /**
     * * Return a logger named according to the name parameter using the statically bound
     * {@link ILoggerFactory} instance.
     *
     * <span style="color:green">final static Logger logger = LogManager.getCurrentLogger();</span>
     *
     * @return logger
     */
    public static Logger getCurrentClassLogger() {
        StackTraceElement frame = (new Throwable()).getStackTrace()[1];
        return LoggerFactory.getLogger(frame.getClassName());
    }

    /**
     * 获取调用者的当前类名。
     *
     * @return 放回调用者的类全名。
     */
    public static String getCurrentClass() {
        StackTraceElement frame = (new Throwable()).getStackTrace()[1];
        return frame.getClassName();
    }

    /**
     * Return a logger named corresponding to the class passed as parameter, using the statically
     * bound {@link ILoggerFactory} instance.
     *
     * @param clazz the returned logger will be named after clazz
     * @return logger
     */
    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * Return a logger named according to the name parameter using the statically bound
     * {@link ILoggerFactory} instance.
     *
     * @param name The name of the logger.
     * @return logger
     */
    public static Logger getLogger(String name) {
        return LoggerFactory.getLogger(name);
    }
}
