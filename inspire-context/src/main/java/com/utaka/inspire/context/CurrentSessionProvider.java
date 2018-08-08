/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.context;


import com.utaka.inspire.security.AuthenticationToken;

import java.util.Set;

/**
 * 回话提供者
 *
 * @author lanxe
 */
public interface CurrentSessionProvider<T> {

    /**
     * 获取当前的回话
     *
     * @return 返回指定类型的回话对象引用。
     */
    T getSession();

    /**
     * 判断当前的回话是否已经验证
     */
    boolean isAuthenticated();

    /**
     * 获取当前会话关联的身份。
     */
    String getIdentity();

    /**
     * 在当前会话中设置的关联身份。
     */
    void setIdentity(String identity);

    /**
     * 获取当前回话关联的用户名称
     */
    String getUserName();

    /**
     * 在当前会话中设置关联的用户名。
     */
    void setUserName(String userName);

    /**
     * 获取当前会话关联的用户组。
     */
    Set<String> getGroup();

    /**
     * 在当前会话中设置关联的用户组。
     */
    void setGroup(Set<String> group);

    /**
     * 获取当前会话关联的用户权限标识。
     */
    Set<String> getPrincipals();

    /**
     * 在当前会话中设置关联的权限标识。
     */
    void setPrincipals(Set<String> principals);


    /**
     * 注销
     */
    void unregister();

    /**
     * 在当前的回话中注册身份登录信息
     *
     * @param tonken 身份验证返回的信息。
     */
    void register(AuthenticationToken tonken);

}
