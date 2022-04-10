package com.peachdevops.community.exception;

public class VerificationCodeAlreadyUsedException extends RuntimeException {
    public VerificationCodeAlreadyUsedException(String message) {
        super(message);
    }

    public VerificationCodeAlreadyUsedException() {
        super("이미 인증된 코드");
    }
}
