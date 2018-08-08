/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.cache;

/**
 * 缓存提供程序接口定义。
 *
 * @author XINEN
 */
public interface CacheProvider<K, V> {

    /**
     * 根据指定的key获取缓存对象。
     */
    V get(K key);

    /**
     * 缓存指定的对象。
     */
    V set(K key, V value);

    /**
     * 从缓存中移除指定的对象。
     */
    void remove(K key);

    /**
     * 清空缓存
     */
    void clean();

}
