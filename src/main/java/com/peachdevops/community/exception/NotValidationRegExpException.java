package com.peachdevops.community.exception;

public class NotValidationRegExpException extends RuntimeException{
    public NotValidationRegExpException(String message) {
        super(message);
    }

    public NotValidationRegExpException() {
        super("NotValidationRegExp");
    }
}
