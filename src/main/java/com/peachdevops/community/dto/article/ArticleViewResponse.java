package com.peachdevops.community.dto.article;

import java.time.LocalDateTime;

public record ArticleViewResponse(
        Long id,
        String boardCode,
        String title,
        String nickname,
        String content,
        LocalDateTime writeAt,
        LocalDateTime modifyAt,
        Integer view,
        Boolean isDeleted
) {
    public ArticleViewResponse(Long id, String boardCode,String title, String nickname,  String content, LocalDateTime writeAt, LocalDateTime modifyAt, Integer view, Boolean isDeleted) {
        this.id = id;
        this.boardCode = boardCode;
        this.title = title;
        this.nickname = nickname;
        this.content = content;
        this.writeAt = writeAt;
        this.modifyAt = modifyAt;
        this.view = view;
        this.isDeleted = isDeleted;
    }

    public static ArticleViewResponse of(
            Long id,
            String boardCode,
            String title,
            String nickname,
            String content,
            LocalDateTime writeAt,
            LocalDateTime modifyAt,
            Integer view,
            Boolean isDeleted
    ) {
        return new ArticleViewResponse(
                id,
                boardCode,
                title,
                nickname,
                content,
                writeAt,
                modifyAt,
                view,
                isDeleted
        );
    }

    public static ArticleViewResponse from(ArticleDto articleDto) {
        if (articleDto == null) {
            return null;
        }
        return ArticleViewResponse.of(
                articleDto.id(),
                articleDto.boardCode(),
                articleDto.title(),
                articleDto.nickname(),
                articleDto.content(),
                articleDto.writeAt(),
                articleDto.modifyAt(),
                articleDto.view(),
                articleDto.isDeleted()
        );
    }
}
