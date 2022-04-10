package com.peachdevops.community.exception;

public class VerificationCodeNotFoundException extends RuntimeException {
    public VerificationCodeNotFoundException(String message) {
        super(message);
    }

    public VerificationCodeNotFoundException() {
        super("알수없는 코드");
    }
}
