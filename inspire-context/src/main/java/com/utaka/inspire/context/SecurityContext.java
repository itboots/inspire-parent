/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.context;

import com.utaka.inspire.security.CredentialManager;
import com.utaka.inspire.security.IdentityManager;
import com.utaka.inspire.security.PrincipalManager;
import com.utaka.inspire.security.PrivilegeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 安全管理上下文
 */
@Component
public class SecurityContext {

    /**
     * 会话管理
     */
    @Autowired(required = false)
    private SessionManager<?> sessionManager;

    /**
     * 身份管理
     */
    @Autowired(required = false)
    private IdentityManager<?> identityManager;

    /**
     * 权限管理
     */
    @Autowired(required = false)
    private PrivilegeManager<?> privilegeManager;

    /**
     * 凭证管理
     */
    @Autowired(required = false)
    private CredentialManager credentialManager;

    /**
     * 身份标识（角色）管理
     */
    @Autowired(required = false)
    private PrincipalManager principalManager;

    /**
     * 获取当前应用会话管理对象。
     */
    @SuppressWarnings("unchecked")
    public <T> SessionManager<T> getSessionManager() {
        return (SessionManager<T>) sessionManager;
    }

    /**
     * 获取身份管理对象。
     */
    @SuppressWarnings("unchecked")
    public <T> IdentityManager<T> getIdentityManager() {
        return (IdentityManager<T>) identityManager;
    }

    /**
     * 获取凭证管理对象
     */
    @SuppressWarnings("unchecked")
    public CredentialManager getCredentialManager() {
        return credentialManager;
    }

    /**
     * 获取当前权限管理对象。
     */
    @SuppressWarnings("unchecked")
    public <T> PrivilegeManager<T> getPrivilegeManager() {
        return (PrivilegeManager<T>) privilegeManager;
    }

    public PrincipalManager getPrincipalManager() {
        return principalManager;
    }


    /**
     * 清空当前的用户和权限标记。
     */
    public void clean() {
        if (this.identityManager != null) {
            this.identityManager.cleanIdentity();
        }
        if (this.privilegeManager != null) {
            this.privilegeManager.cleanPrivilege();
        }
    }

}
