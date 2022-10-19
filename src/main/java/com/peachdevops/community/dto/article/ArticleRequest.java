package com.peachdevops.community.dto.article;

public record ArticleRequest(
        Long id,
        String title,
        String content,
        String docsType,
        String tag,
        String nickname
) {
    public static ArticleRequest of(
            Long id,
            String title,
            String content,
            String docsType,
            String tag,
            String nickname
    ) {
        return new ArticleRequest(
                id,
                title,
                content,
                docsType,
                tag,
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
                this.tag(),
                null,
                null,
                this.docsType(),
                null,
                null,
                null,
                null,
                null
        );
    }

}
