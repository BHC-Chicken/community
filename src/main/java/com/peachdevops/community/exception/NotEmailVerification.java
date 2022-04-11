package com.peachdevops.community.exception;

public class NotEmailVerification extends RuntimeException{
    public NotEmailVerification(String message) {
        super(message);
    }

    public NotEmailVerification() {
        super("이메일 미인증");
    }
}
