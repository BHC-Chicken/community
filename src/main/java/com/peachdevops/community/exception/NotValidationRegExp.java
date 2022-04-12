package com.peachdevops.community.exception;

public class NotValidationRegExp extends RuntimeException{
    public NotValidationRegExp(String message) {
        super(message);
    }

    public NotValidationRegExp() {
        super("정규식 틀림");
    }
}
