/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.security;


import java.util.Set;

/**
 * 用户组管理
 * <p>
 * 由应用实现
 */
public interface GroupManager {

    /**
     * 根据指定角色获取角色名称
     *
     * @param groupCodeSet 取值{@link CurrentSessionProvider#getGroup()}
     * @return 返回指定用户的权限标识名称集合
     */
    Set<String> getGroupNameSet(Set<String> groupCodeSet);

}
