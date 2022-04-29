package com.peachdevops.community.exception;

public class VerificationCodeTimeOutException extends RuntimeException{
    public VerificationCodeTimeOutException(String message) {
        super(message);
    }

    public VerificationCodeTimeOutException() {
        super("인증시간 만료");
    }
}
