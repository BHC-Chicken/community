package com.peachdevops.community.dto.article;

import com.peachdevops.community.domain.Article;

import java.time.LocalDateTime;

public record ArticleDto(
        Long id,
        String nickname,
        String boardCode,
        String title,
        String content,
        LocalDateTime writeAt,
        LocalDateTime modifyAt,
        String docsType,
        Integer view,
        Boolean isDeleted,
        Boolean isNotice,
        Long recommendCount,
        String sentiment
) {
    public static ArticleDto of(
            Long id,
            String nickname,
            String boardCode,
            String title,
            String content,
            LocalDateTime writeAt,
            LocalDateTime modifyAt,
            String docsType,
            Integer view,
            Boolean isDeleted,
            Boolean isNotice,
            Long recommendCount,
            String sentiment
    ) {
        return new ArticleDto(
                id,
                nickname,
                boardCode,
                title,
                content,
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

    public static ArticleDto of(Article article) {
        return new ArticleDto(
                article.getId(),
                article.getNickname(),
                article.getBoardCode(),
                article.getTitle(),
                article.getContent(),
                article.getWriteAt(),
                article.getModifyAt(),
                article.getDocsType(),
                article.getView(),
                article.getIsDeleted(),
                article.getIsNotice(),
                article.getRecommendCount(),
                article.getSentiment()
        );
    }

    public Article toEntity(Article article) {
        return Article.of(
                id,
                nickname,
                boardCode,
                title,
                content,
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

    public Article updateEntity(Article article) {
        if (boardCode != null) {
            article.setBoardCode(boardCode);
        }
        if (title != null) {
            article.setTitle(title);
        }
        if (content != null) {
            article.setContent(content);
        }

        return article;
    }
}
