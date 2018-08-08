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
public interface PrivilegeManager<T> {

    /**
     * 获取指定用户组的权限
     */
    public Set<T> getPrivilege(Set<String> principals);


    /**
     * 清空所有的权限信息
     */
    public PrivilegeManager<T> cleanPrivilege();

}
