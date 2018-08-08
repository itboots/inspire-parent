/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.event;

/**
 * 用于指定异步消息的目标名称
 */
public interface DestinationNameResolver {

    /**
     * 获取异步消息存储的目标名称
     */
    String getDesttinationName();
}
