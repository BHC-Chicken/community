package com.peachdevops.community.dto;

import com.peachdevops.community.domain.Article;
import com.peachdevops.community.domain.User;

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
        Boolean isDeleted
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
            Boolean isDeleted
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
                isDeleted
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
                article.getIsDeleted()
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
                isDeleted
        );
    }

    public Article updateEntity(Article article) {
        if (boardCode != null) {article.setBoardCode(boardCode);}
        if (title != null) {article.setTitle(title);}
        if (content != null) {article.setContent(content);}

        return article;
    }
}
