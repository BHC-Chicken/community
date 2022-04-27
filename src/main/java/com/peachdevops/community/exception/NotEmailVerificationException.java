package com.peachdevops.community.exception;

public class NotEmailVerificationException extends RuntimeException{
    public NotEmailVerificationException(String message) {
        super(message);
    }

    public NotEmailVerificationException() {
        super("이메일 미인증");
    }
}
