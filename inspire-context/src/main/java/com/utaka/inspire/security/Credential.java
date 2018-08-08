/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.security;

import java.io.Serializable;

/**
 * 身份凭证接口
 *
 * @author LANXE
 */
public interface Credential extends Serializable {
    String getId();
}
