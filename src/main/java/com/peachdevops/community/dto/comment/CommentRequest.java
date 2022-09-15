package com.peachdevops.community.dto.comment;

public record CommentRequest(
        Long id,
        String content,
        String nickname,
        Long articleId
) {
    public static CommentRequest of(
            Long id,
            String content,
            String nickname,
            Long articleId
    ) {
        return new CommentRequest(
                id,
                content,
                nickname,
                articleId
        );
    }

    public CommentDto toDto() {
        return CommentDto.of(
                null,
                nickname,
                content,
                articleId,
                null,
                null,
                null
        );
    }
}
