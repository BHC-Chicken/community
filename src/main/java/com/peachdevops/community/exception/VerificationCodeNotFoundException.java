package com.peachdevops.community.exception;

public class VerificationCodeNotFoundException extends RuntimeException {
    public VerificationCodeNotFoundException(String message) {
        super(message);
    }

    public VerificationCodeNotFoundException() {
        super("이미 등록된 유저입니다.");
    }
}
