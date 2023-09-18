package com.modu.MemberServer.exception;

public class PasswordNotSatisfiedException extends RuntimeException {
    public PasswordNotSatisfiedException(String msg) {
        super(msg);
    }
}
