package com.peachdevops.community.dto;

import java.time.LocalDateTime;

public record ArticleViewResponse(
        Long id,
        String title,
        String content,
        LocalDateTime writeTime,
        LocalDateTime modifyTime,
        Integer view,
        Boolean isDeleted
) {
    public ArticleViewResponse(Long id, String title, String content, LocalDateTime writeTime, LocalDateTime modifyTime, Integer view, Boolean isDeleted) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writeTime = writeTime;
        this.modifyTime = modifyTime;
        this.view = view;
        this.isDeleted = isDeleted;
    }

    public static ArticleViewResponse of(
            Long id,
            String title,
            String content,
            LocalDateTime writeTime,
            LocalDateTime modifyTime,
            Integer view,
            Boolean isDeleted
    ) {
        return new ArticleViewResponse(
                id,
                title,
                content,
                writeTime,
                modifyTime,
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
                articleDto.title(),
                articleDto.content(),
                articleDto.writeTime(),
                articleDto.modifyTime(),
                articleDto.view(),
                articleDto.isDeleted()
        );
    }
}
