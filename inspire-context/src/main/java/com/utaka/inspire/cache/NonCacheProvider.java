/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.cache;

import com.google.common.cache.CacheLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author lanxe
 */
public class NonCacheProvider<K, V> implements CacheProvider<K, V> {
    private static Logger LOG = LoggerFactory.getLogger(NonCacheProvider.class);

    private final CacheLoader<K, V> loader;

    public NonCacheProvider(CacheLoader<K, V> loader) {
        this.loader = loader;
    }

    @Override
    public V get(K key) {
        if (loader != null) {
            try {
                return loader.load(checkNotNull(key));
            } catch (Exception e) {
                LOG.error("value loader fail!", e);
            }
        }
        return null;
    }

    @Override
    public V set(K key, V value) {
        return value;
    }

    @Override
    public void remove(K key) {

    }

    @Override
    public void clean() {

    }
}
