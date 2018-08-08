/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.event;

/**
 * 用于指定异步消息的延迟时间
 */
public interface DeliveryDelaySupport {

    /**
     * 获取异步消息延迟时间（秒），
     */
    int getDeliveryDelay();
}
