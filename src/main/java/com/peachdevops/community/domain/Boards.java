package com.peachdevops.community.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
public class Boards {

    @Id
    String boardCode;
    String boardName;

    public Boards(String boardCode, String boardName) {
        this.boardCode = boardCode;
        this.boardName = boardName;
    }

    public Boards() {
    }
}
