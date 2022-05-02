package com.peachdevops.community.dto;

import com.peachdevops.community.domain.User;

public record ArticleRequest(
        Long id,
        String title,
        String content,
        String nickname
) {
    public static ArticleRequest of(
            Long id,
            String title,
            String content,
            String nickname
    ) {
        return new ArticleRequest(
                id,
                title,
                content,
                nickname
        );
    }

    public ArticleDto toDto() {
        return ArticleDto.of(
                null,
                nickname,
                null,
                this.title(),
                this.content(),
                null,
                null,
                null,
                null
        );
    }

}
