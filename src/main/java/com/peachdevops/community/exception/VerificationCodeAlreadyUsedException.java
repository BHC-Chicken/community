package com.peachdevops.community.exception;

public class VerificationCodeAlreadyUsedException extends RuntimeException {
    public VerificationCodeAlreadyUsedException(String message) {
        super(message);
    }

    public VerificationCodeAlreadyUsedException() {
        super("이미 등록된 유저입니다.");
    }
}
