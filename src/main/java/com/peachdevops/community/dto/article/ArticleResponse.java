package com.peachdevops.community.dto.article;

import java.time.LocalDateTime;

public record ArticleResponse(
        Long id,
        String nickname,
        String title,
        String content,
        String tag,
        String boardCode,
        LocalDateTime writeAt,
        LocalDateTime modifyAt,
        String docsType,
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
            String tag,
            String boardCode,
            LocalDateTime writeAt,
            LocalDateTime modifyAt,
            String docsType,
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
                tag,
                boardCode,
                writeAt,
                modifyAt,
                docsType,
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
                articleDto.tag(),
                articleDto.boardCode(),
                articleDto.writeAt(),
                articleDto.modifyAt(),
                articleDto.docsType(),
                articleDto.isDeleted(),
                articleDto.view(),
                articleDto.recommendCount(),
                articleDto.sentiment()
        );
    }
}
