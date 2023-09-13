package com.modu.MemberServer.exception;

public class EmptyMemberException extends RuntimeException {
    public EmptyMemberException(String msg) {
        super(msg);
    }
}
