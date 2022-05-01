package com.peachdevops.community.exception;

public class DataAccessErrorException extends RuntimeException{
    public DataAccessErrorException(String message) {
        super(message);
    }

    public DataAccessErrorException() {
        super("Data Access Error Exception");
    }
}
