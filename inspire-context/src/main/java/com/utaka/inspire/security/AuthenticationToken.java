/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.security;

import java.util.Set;

/**
 * 身份验证的标记。作为验证身份后返回的结果。
 *
 * @author lanxe
 */
public interface AuthenticationToken {

    /**
     * 获取凭证的唯一标识。
     */
    String getId();

    /**
     * 获取当前凭证关联的身份标识。
     */
    String getIdentity();

    /**
     * 获取该用户显示的名称。
     */
    String getName();

    /**
     * 获取当前凭证关联的用户组。
     */
    Set<String> getGroup();

    /**
     * 获取当前凭证关联的权限标识。
     */
    Set<String> getPrincipals();

    /**
     * 指示当前凭证是否是临时凭证。
     */
    boolean isTemporary();

}
