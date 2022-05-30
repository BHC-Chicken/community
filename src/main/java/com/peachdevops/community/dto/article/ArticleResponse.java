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
        Boolean isDeleted,
        Integer view,
        Long recommendCount,
        String sentiment
) {

    public static ArticleResponse of(
            Long id,
            String nickname,
            String title,
            String content,
            String boardCode,
            LocalDateTime writeAt,
            LocalDateTime modifyAt,
            Boolean isDeleted,
            Integer view,
            Long recommendCount,
            String sentiment
    ) {
        return new ArticleResponse(
                id,
                nickname,
                title,
                content,
                boardCode,
                writeAt,
                modifyAt,
                isDeleted,
                view,
                recommendCount,
                sentiment
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
                articleDto.isDeleted(),
                articleDto.view(),
                articleDto.recommendCount(),
                articleDto.sentiment()
        );
    }
}
