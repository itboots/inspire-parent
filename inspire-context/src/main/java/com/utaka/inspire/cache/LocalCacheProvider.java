/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author XINEN
 */
public class LocalCacheProvider<K, V> implements CacheProvider<K, V> {

    public static final long DEFAULT_DURATION = 20;
    public static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;

    private final Cache<K, V> cache;

    public LocalCacheProvider() {
        cache = CacheBuilder
                .newBuilder()
//                .weakKeys()
                .softValues()
                .expireAfterAccess(DEFAULT_DURATION, DEFAULT_TIME_UNIT)
                .build();
    }

    public LocalCacheProvider(long duration, TimeUnit unit) {
        cache = CacheBuilder
                .newBuilder()
//                .weakKeys()
                .softValues()
                .expireAfterAccess(duration, unit)
                .build();
    }

    public LocalCacheProvider(CacheLoader<K, V> loader) {
        cache = CacheBuilder
                .newBuilder()
//                .weakKeys()
                .softValues()
                .expireAfterAccess(DEFAULT_DURATION, DEFAULT_TIME_UNIT)
                .build(loader);
    }

    public LocalCacheProvider(long duration, TimeUnit unit, CacheLoader<K, V> loader) {
        cache = CacheBuilder
                .newBuilder()
//                .weakKeys()
                .softValues()
                .expireAfterAccess(duration, unit)
                .build(loader);
    }

    /*
     * (non-Javadoc)
     *
     * @see CacheProvider#get(java.lang.Object)
     */
    @Override
    public V get(K key) {
        if (ObjectUtils.isEmpty(key)) {
            return null;
        }
        if (cache instanceof LoadingCache) {
            try {
                return ((LoadingCache<K, V>) cache).get(key);

            } catch (ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return cache.getIfPresent(key);

        }

    }

    /*
     * (non-Javadoc)
     *
     */
    @Override
    public V set(K key, V value) {
        cache.put(key, value);
        return value;
    }

    /*
     * (non-Javadoc)
     *
     */
    @Override
    public void remove(K key) {
        cache.invalidate(key);

    }

    @Override
    public void clean() {
        cache.invalidateAll();

    }

}
