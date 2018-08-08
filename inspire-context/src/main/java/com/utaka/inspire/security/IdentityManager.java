/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.security;

/**
 * 身份标识
 *
 * @author LANXE
 */
public interface IdentityManager<T> {

    /**
     * 获取当前身份
     */
    public T get(String id);

    /**
     * 更新身份信息
     *
     * @param identity 要更新的身份信息
     */
    public T update(T identity);

    /**
     * 移除指定的用户
     */
    public IdentityManager<T> remove(String id);

    /**
     * 清空所有的身份信息
     */
    public IdentityManager<T> cleanIdentity();

}
