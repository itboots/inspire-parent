/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.security;

/**
 * 用户密码身份凭证
 *
 * @author LANXE
 */
public interface UserPasswordCredential extends Credential {

    String getUser();

    String getPassword();

    void setPassword(String password);

    String hashPassword(String user, String password);

    String getTemporaryPassword();

    void setTemporaryPassword(String value);
}
