package com.peachdevops.community.exception;

public class NotFoundBoard extends RuntimeException {
    public NotFoundBoard(String message) {
        super(message);
    }

    public NotFoundBoard() {
        super("존재하지 않는 게시판");
    }
}

