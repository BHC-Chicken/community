package com.peachdevops.community.dto.comment;

import com.peachdevops.community.domain.Comment;

import java.time.LocalDateTime;

public record CommentDto(
        Long id,
        String nickname,
        String content,
        Long articleId,
        LocalDateTime writeAt,
        LocalDateTime modifyAt,
        Boolean isDeleted
) {
    public static CommentDto of(
            Long id,
            String nickname,
            String content,
            Long articleId,
            LocalDateTime writeAt,
            LocalDateTime modifyAt,
            Boolean isDeleted
    ) {
        return new CommentDto(
                id,
                nickname,
                content,
                articleId,
                writeAt,
                modifyAt,
                isDeleted
        );
    }

    public static CommentDto of(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getNickname(),
                comment.getContent(),
                comment.getArticleId(),
                comment.getWriteAt(),
                comment.getModifyAt(),
                comment.getIsDeleted()
        );
    }

    public Comment updateEntity(Comment comment) {
        if (content != null) {comment.setContent(content);}

        return comment;

    }
}
