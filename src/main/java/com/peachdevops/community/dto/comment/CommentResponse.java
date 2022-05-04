package com.peachdevops.community.dto.comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String nickname,
        String content,
        Long articleId,
        LocalDateTime modifyAt
) {

    public static CommentResponse of(
            Long id,
            String nickname,
            String content,
            Long articleId,
            LocalDateTime modifyAt
    ) {
        return new CommentResponse(
                id,
                nickname,
                content,
                articleId,
                modifyAt);
    }

    public static CommentResponse from(CommentDto commentDto) {
        if (commentDto == null) {
            return null;
        }
        return CommentResponse.of(
                commentDto.id(),
                commentDto.nickname(),
                commentDto.content(),
                commentDto.articleId(),
                commentDto.modifyAt()
        );
    }
}
