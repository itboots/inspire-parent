/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

/**
 *
 */
package com.utaka.inspire.event;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.utaka.inspire.util.LogManager;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 统一在这边监听异步事件，并在处理的时候再转化为同步事件。
 *
 * @author lanxe
 */
public class AsyncEventListener implements InitializingBean, DisposableBean {

    private static final Logger LOG = LogManager.getCurrentClassLogger();

    @Autowired(required = false)
    protected EventBusService bus;

    @Subscribe
    @AllowConcurrentEvents
    public void onAsyncEvent(Object event) {
        try {
            if (event != null) {
                this.enqueue(event);
                LOG.info("###handle async event with ({}) success!", event);
            }
        } catch (Exception e) {
            LOG.error("###handle async event ({}) fail!", event);
            throw new Error(e);
        }
    }

    private void enqueue(Object event) {
        bus.post(event);

    }

    @Override
    public void destroy() throws Exception {
        this.bus.unregisterAsync(this);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.bus == null) {
            this.bus = DefaultEventBusService.getInstance();
        }
        this.bus.registerAsync(this);

    }
}
