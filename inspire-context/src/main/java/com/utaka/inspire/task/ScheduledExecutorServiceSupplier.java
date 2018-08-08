/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.task;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 执行定时任务的提供类
 *
 * @author XINEN
 */
public interface ScheduledExecutorServiceSupplier {

    /**
     * 获取 {@link ScheduledExecutorService}对象.
     *
     * @return
     */
    ScheduledExecutorService getScheduledExecutorService();

    /**
     * 提交要执行的任务
     *
     * @param task
     * @return
     */
    Future<?> submit(Runnable task);

    /**
     * 根据指定的步长定期执行指定的任务，严格按照执行结束后的时间计时。
     *
     * @param command      要执行的命令
     * @param initialDelay 初始延迟时长
     * @param delay        步长
     * @param unit         计时单位
     * @return 返回一个可以控制运行状态的 {@link ScheduledFuture }
     */
    ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
                                              long initialDelay,
                                              long delay,
                                              TimeUnit unit);

}
