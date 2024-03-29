package com.peachdevops.community.dto.article;

import java.time.LocalDateTime;

public record ArticleViewResponse(
        Long id,
        String boardCode,
        String title,
        String nickname,
        String content,
        String tag,
        LocalDateTime writeAt,
        LocalDateTime modifyAt,
        String docsType,
        Integer view,
        Boolean isDeleted,
        Boolean isNotice,
        Long recommendCount,
        String sentiment
) {
    public ArticleViewResponse(Long id,
                               String boardCode,
                               String title,
                               String nickname,
                               String content,
                               String tag,
                               LocalDateTime writeAt,
                               LocalDateTime modifyAt,
                               String docsType,
                               Integer view,
                               Boolean isDeleted,
                               Boolean isNotice,
                               Long recommendCount,
                               String sentiment
    ) {
        this.id = id;
        this.boardCode = boardCode;
        this.title = title;
        this.nickname = nickname;
        this.content = content;
        this.tag = tag;
        this.writeAt = writeAt;
        this.modifyAt = modifyAt;
        this.docsType = docsType;
        this.view = view;
        this.isDeleted = isDeleted;
        this.isNotice = isNotice;
        this.recommendCount = recommendCount;
        this.sentiment = sentiment;
    }

    public static ArticleViewResponse of(
            Long id,
            String boardCode,
            String title,
            String nickname,
            String content,
            String tag,
            LocalDateTime writeAt,
            LocalDateTime modifyAt,
            String docsType,
            Integer view,
            Boolean isDeleted,
            Boolean isNotice,
            Long recommendCount,
            String sentiment
    ) {
        return new ArticleViewResponse(
                id,
                boardCode,
                title,
                nickname,
                content,
                tag,
                writeAt,
                modifyAt,
                docsType,
                view,
                isDeleted,
                isNotice,
                recommendCount,
                sentiment
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
                articleDto.tag(),
                articleDto.writeAt(),
                articleDto.modifyAt(),
                articleDto.docsType(),
                articleDto.view(),
                articleDto.isDeleted(),
                articleDto.isNotice(),
                articleDto.recommendCount(),
                articleDto.sentiment()
        );
    }
}
