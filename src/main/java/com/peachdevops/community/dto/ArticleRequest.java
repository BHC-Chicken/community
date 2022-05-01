package com.peachdevops.community.dto;

import com.peachdevops.community.domain.User;

public record ArticleRequest(
        Long Id,
        String title,
        String content,
        User user
) {
    public static ArticleRequest of(
            Long Id,
            String title,
            String content,
            User user
    ) {
        return new ArticleRequest(
                Id,
                title,
                content,
                user
        );
    }

    public ArticleDto toDto() {
        return ArticleDto.of(
                null,
                this.user(),
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
