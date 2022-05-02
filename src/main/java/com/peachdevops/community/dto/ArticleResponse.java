package com.peachdevops.community.dto;

import com.peachdevops.community.domain.User;

import java.time.LocalDateTime;

public record ArticleResponse(
        Long id,
        String nickname,
        String title,
        String content,
        String boardCode,
        LocalDateTime writeAt,
        LocalDateTime modifyAt,
        Integer view
) {

    public static ArticleResponse of(
            Long id,
            String nickname,
            String title,
            String content,
            String boardCode,
            LocalDateTime writeAt,
            LocalDateTime modifyAt,
            Integer view
    ) {
        return new ArticleResponse(
                id,
                nickname,
                title,
                content,
                boardCode,
                writeAt,
                modifyAt,
                view);
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
                articleDto.view()
        );
    }
}
