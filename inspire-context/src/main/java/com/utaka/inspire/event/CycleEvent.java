/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.event;

/**
 * 循环事件
 * <p>
 * Created by lanxe on 2016/4/19.
 */
public abstract class CycleEvent implements DestinationNameResolver, DeliveryDelaySupport {

    /**
     * 发送通知重试次数
     */
    private int tryCount = 0;

    protected void setTryCount(int count) {
        this.tryCount = count;
    }

    public int getTryCount() {
        return this.tryCount;
    }

    public CycleEvent increment() {
        this.tryCount++;
        return this;

    }

    /**
     * 获取异步消息延迟时间（秒），
     */
    public int getDeliveryDelay() {
        if (tryCount < 3) {
            return 10;
        } else if (tryCount < 5) {
            return 60; //1分钟
        } else {
            return 5 * 60;//5分钟
        }
    }

}
