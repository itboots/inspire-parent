/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.event;

import java.util.Collection;

/**
 * 消息关键词，查询消息使用
 *
 * @author lanxe
 */
public interface KeyResolver {

    /**
     * 获取查询消息关键词
     */
    Collection<String> getKeys();
}
