package com.peachdevops.community.repository.querydsl;

import com.peachdevops.community.domain.Boards;
import com.peachdevops.community.domain.QBoards;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class BoardsRepositoryCustomImpl extends QuerydslRepositorySupport implements BoardsRepositoryCustom {

    public BoardsRepositoryCustomImpl() {
        super(Boards.class);
    }


    @Override
    public String findBoardNameByBoardCode(String boardCode) {
        QBoards boards = QBoards.boards;

        return from(boards)
                .select(boards.boardName)
                .where(boards.boardCode.eq(boardCode))
                .fetch().toString();
    }
}
