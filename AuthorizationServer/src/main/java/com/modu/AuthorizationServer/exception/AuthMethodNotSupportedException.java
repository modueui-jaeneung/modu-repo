package com.modu.authorizationServer.exception;

public class AuthMethodNotSupportedException extends RuntimeException {
    public AuthMethodNotSupportedException(String msg) {
        super(msg);
    }
}
