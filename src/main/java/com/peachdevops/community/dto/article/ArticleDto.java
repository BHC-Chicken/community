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
        Integer view,
        Boolean isDeleted,
        Boolean isNotice,
        Long recommendCount,
        Float sentimentScore
) {
    public static ArticleDto of(
            Long id,
            String nickname,
            String boardCode,
            String title,
            String content,
            LocalDateTime writeAt,
            LocalDateTime modifyAt,
            Integer view,
            Boolean isDeleted,
            Boolean isNotice,
            Long recommendCount,
            Float sentimentScore
    ) {
        return new ArticleDto(
                id,
                nickname,
                boardCode,
                title,
                content,
                writeAt,
                modifyAt,
                view,
                isDeleted,
                isNotice,
                recommendCount,
                sentimentScore
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
                article.getView(),
                article.getIsDeleted(),
                article.getIsNotice(),
                article.getRecommendCount(),
                article.getSentimentScore()
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
                view,
                isDeleted,
                isNotice,
                recommendCount,
                sentimentScore
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
