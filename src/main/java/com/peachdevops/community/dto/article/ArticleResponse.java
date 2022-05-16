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
        Long recommendCount,
        Float sentimentScore
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
            Long recommendCount,
            Float sentimentScore
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
                recommendCount,
                sentimentScore
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
                articleDto.recommendCount(),
                articleDto.sentimentScore()
        );
    }
}
