/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.event;

import com.google.common.eventbus.AsyncEventBus;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 事件总线的服务类，用于提供发送事件和注册监听器。
 * <b>不保证异步事务的完整性</b>
 *
 * @author LANXE
 */
public final class DefaultEventBusService extends EventBusManager {

    public static DefaultEventBusService INSTANCE = new DefaultEventBusService();

    public DefaultEventBusService() {
        super(
                new AsyncEventBus(new ThreadPoolExecutor(1, 1,
                        0L, TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>()))
        );
    }
}
