/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.event;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 抽象的监听器，继承此类的对象可直接接收来自{@link EventBusService}的同步消息。
 *
 * @author XINEN
 */
public abstract class AbstractListener implements InitializingBean, DisposableBean {

    @Autowired(required = false)
    protected EventBusService bus;

    /*
     * (non-Javadoc)
     *
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.bus == null) {
            this.bus = DefaultEventBusService.getInstance();
        }

        this.bus.register(this);
        this.doAfterPropertiesSet();
    }

    @Override
    public void destroy() throws Exception {
        this.bus.unregister(this);
    }

    protected abstract void doAfterPropertiesSet();

}
