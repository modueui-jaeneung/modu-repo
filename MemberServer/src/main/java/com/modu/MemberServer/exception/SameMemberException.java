package com.modu.MemberServer.exception;

public class SameMemberException extends RuntimeException {
    public SameMemberException(String message) {
        super(message);
    }
}
