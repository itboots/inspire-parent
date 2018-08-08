/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.security;

import java.util.Set;

/**
 * 权限管理
 *
 * @author LANXE
 */
public interface CredentialManager {

    /**
     * 获取指定用户的凭证提供者列表
     */
    Set<CredentialProvider<?>> getCredential(String user);


    <T> T getCredentialProvider(Class<T> providerClass);

}
