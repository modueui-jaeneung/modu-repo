package com.modu.MemberServer.exception;

public class EmailFormatNotSatisfiedException extends RuntimeException {
    public EmailFormatNotSatisfiedException(String msg) {
        super(msg);
    }
}
