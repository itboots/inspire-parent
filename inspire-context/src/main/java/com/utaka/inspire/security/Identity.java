/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.security;

import java.io.Serializable;
import java.security.Principal;

/**
 * 用户身份唯一标识
 *
 * @author LANXE
 */
public interface Identity extends Principal, Serializable {

    /**
     * 获取表示当前身份的唯一代码
     */
    String getCode();

    /**
     * 指示当前身份是否激活状态
     */
    boolean isActived();
}
