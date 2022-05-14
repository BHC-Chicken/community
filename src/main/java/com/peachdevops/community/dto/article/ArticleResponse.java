package com.peachdevops.community.dto.article;

import java.time.LocalDateTime;

public record ArticleResponse(
        Long id,
        String nickname,
        String title,
        String content,
        String boardCode,
        LocalDateTime writeAt,
        LocalDateTime modifyAt,
        Integer view,
        Long recommendCount
) {

    public static ArticleResponse of(
            Long id,
            String nickname,
            String title,
            String content,
            String boardCode,
            LocalDateTime writeAt,
            LocalDateTime modifyAt,
            Integer view,
            Long recommendCount
    ) {
        return new ArticleResponse(
                id,
                nickname,
                title,
                content,
                boardCode,
                writeAt,
                modifyAt,
                view,
                recommendCount
        );
    }

    public static ArticleResponse from(ArticleDto articleDto) {
        if (articleDto == null) {
            return null;
        }
        return ArticleResponse.of(
                articleDto.id(),
                articleDto.nickname(),
                articleDto.title(),
                articleDto.content(),
                articleDto.boardCode(),
                articleDto.writeAt(),
                articleDto.modifyAt(),
                articleDto.view(),
                articleDto.recommendCount()
        );
    }
}
