/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 提供当前执行上下文的环境。
 *
 * @author lanxe
 */
@Component
public final class ContextHolder implements ApplicationContextAware {

    private static ApplicationContext springContext;

    private static SecurityContext securityContext;


    /**
     * 获取当前的Spring上下文
     */
    public static ApplicationContext getSpringContext() {
        return springContext;
    }

    /**
     * 获取当前应用会话管理对象。
     */
    public static SecurityContext getSecurityContext() {
        return securityContext;
    }


    @Override
    public synchronized void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        springContext = applicationContext;
    }

    @Autowired
    public synchronized void setSecurityContext(SecurityContext context) {
        securityContext = context;
    }

}
