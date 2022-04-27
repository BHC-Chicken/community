package com.peachdevops.community.exception;

public class VerificationCodeAlreadyUsedException extends RuntimeException {
    public VerificationCodeAlreadyUsedException(String message) {
        super(message);
    }

    public VerificationCodeAlreadyUsedException() {
        super("AlreadyUsedCode");
    }
}
