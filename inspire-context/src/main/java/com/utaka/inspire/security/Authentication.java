/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.security;


import com.utaka.inspire.context.CurrentSessionProvider;

/**
 * 身份验证器，由系统调用，具体应用通过传递{@link AuthenticateFunction}来实现验证逻辑。
 * @author LANXE
 */
public interface Authentication {

    String SUCCESS = "success";

    /**
     * 身份验证使用的凭证不存在
     */
    String CREDENTIAL_NOT_EXIST = "credential.doesnot.exist";

    /**
     * 身份验证使用的凭证已经失效
     */
    String CREDENTIAL_NOT_ACTIVED = "credential.doesnot.actived";

    /**
     * 身份验证使用的凭证已经被锁定，暂时不可用
     */
    String CREDENTIAL_LOCKED = "credential.had.locked";

    /**
     * 身份验证使用的凭证无效
     */
    String CREDENTIAL_INACCURACY = "credential.inaccuracy";

    /**
     * 身份凭证一致
     */
    String CREDENTIAL_SAME = "credential.same";

    /**
     * 身份验证使用的是品是临时凭证
     */
    String CREDENTIAL_TEMPORARY = "credential.temporary";

    /**
     * 身份验证使用的凭证不唯一
     */
    String CREDENTIAL_NOT_UNIQUE = "credential.doesnot.unique";


    /**
     * 验证身份
     *
     * @param function 验证身份的方法，由应用实现。
     * @return 如果是验证通过则，返回空字符串；否则返回错误信息。
     */
    String authenticate(AuthenticateFunction function, CurrentSessionProvider<?> currentSessionProvider);


    /**
     * 用于验证身份的函数
     */
    interface AuthenticateFunction {
        AuthenticationToken authenticate() throws AuthenticationException;
    }

}
