package com.modu.MemberServer.exception;

public class PasswordNotEqualException extends RuntimeException {
    public PasswordNotEqualException(String message) {
        super(message);
    }
}
