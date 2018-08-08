/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.event;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务监听器
 *
 * @author XINEN
 */

public abstract class ScheduledExecutorListener extends ExecutorListener {

    protected ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
                                                        long initialDelay,
                                                        long delay,
                                                        TimeUnit unit) {

        return this.supplier.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

}
