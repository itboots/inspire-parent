/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.security;

import java.security.Principal;
import java.util.Set;

/**
 * 取得当前用户标识。作为权限控制的入口
 * 由应用实现
 *
 * @author lanxe
 */
public interface PrincipalManager extends GroupManager {

    /**
     * 获取指定用户的权限标识。此次可作为角色使用
     *
     * @param user 要获取权限标识的用户
     * @return 返回指定用户的权限标识集合
     */
    Set<Principal> getPrincipals(String user);

}
