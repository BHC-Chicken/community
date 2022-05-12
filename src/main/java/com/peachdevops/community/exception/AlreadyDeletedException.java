package com.peachdevops.community.exception;

public class AlreadyDeletedException extends RuntimeException{
    public AlreadyDeletedException(String message) {
        super(message);
    }

    public AlreadyDeletedException() {
        super("AlreadyDeletedException");
    }
}
