package com.peachdevops.community.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VerificationCodeNotFoundException extends RuntimeException {
    public VerificationCodeNotFoundException(String message) {
        super(message);
    }

    public VerificationCodeNotFoundException() {
        super("CodeNotFound");
    }
}
