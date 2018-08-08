/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.security;

/**
 * Exception to managed errors on the authentication proccess
 */
public class AuthenticationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AuthenticationException() {
        super();

    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);

    }

    public AuthenticationException(String message) {
        super(message);

    }

    public AuthenticationException(Throwable cause) {
        super(cause);

    }

}
