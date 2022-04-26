package com.peachdevops.community.exception;

public class NonExistentCollegeException extends RuntimeException {
    public NonExistentCollegeException(String message) {
        super(message);
    }

    public NonExistentCollegeException() {
        super("존재하지 않는 대학");
    }
}
