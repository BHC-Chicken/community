package com.peachdevops.community.exception;

public class NotFoundBoardException extends RuntimeException {
    public NotFoundBoardException(String message) {
        super(message);
    }

    public NotFoundBoardException() {
        super("존재하지 않는 게시판");
    }
}

