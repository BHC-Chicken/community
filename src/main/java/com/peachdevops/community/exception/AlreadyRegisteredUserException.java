package com.peachdevops.community.exception;

/**
 * 이미 등록된 유저를 재등록하려고 할때 발생하는 Exception
 */
public class AlreadyRegisteredUserException extends RuntimeException {

    public AlreadyRegisteredUserException(String message) {
        super(message);
    }

    public AlreadyRegisteredUserException() {
        super("AlreadyRegisteredUser");
    }
}
