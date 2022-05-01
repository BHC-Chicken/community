package com.peachdevops.community.dto;

import com.peachdevops.community.domain.User;

import java.time.LocalDateTime;

public record ArticleResponse(
        Long id,
        User user,
        String title,
        String content,
        LocalDateTime writeTime,
        LocalDateTime modifyTime,
        Integer view
) {

    public static ArticleResponse of(
            Long id,
            User user,
            String title,
            String content,
            LocalDateTime writeTime,
            LocalDateTime modifyTime,
            Integer view
    ) {
        return new ArticleResponse(
                id,
                user,
                title,
                content,
                writeTime,
                modifyTime,
                view);
    }

    public static ArticleResponse from(ArticleDto articleDto) {
        if (articleDto == null) {
            return null;
        }
        return ArticleResponse.of(
                articleDto.id(),
                new User(),
                articleDto.title(),
                articleDto.content(),
                articleDto.writeTime(),
                articleDto.modifyTime(),
                articleDto.view()
        );
    }
}
