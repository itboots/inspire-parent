/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.event;

import com.utaka.inspire.task.ScheduledExecutorServiceSupplier;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Future;

/**
 * 抽象的同步监听器，继承此类可直接接收{@link EventBusService}的异步消息
 *
 * @author XINEN
 */

public abstract class ExecutorListener extends AbstractListener {

    @Autowired
    protected ScheduledExecutorServiceSupplier supplier;

    protected Future<?> submit(Runnable task) {
        return this.supplier.submit(task);
    }

}
