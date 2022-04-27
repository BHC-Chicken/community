package com.peachdevops.community.exception;

public class NonExistentCollegeException extends RuntimeException {
    public NonExistentCollegeException(String message) {
        super(message);
    }

    public NonExistentCollegeException() {
        super("NonExistentCollege");
    }
}
