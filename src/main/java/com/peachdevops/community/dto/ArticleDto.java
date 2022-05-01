package com.peachdevops.community.dto;

import com.peachdevops.community.domain.Article;
import com.peachdevops.community.domain.User;

import java.time.LocalDateTime;

public record ArticleDto(
        Long id,
        User user,
        String boardCode,
        String title,
        String content,
        LocalDateTime writeTime,
        LocalDateTime modifyTime,
        Integer view,
        Boolean isDeleted
) {
    public static ArticleDto of(
            Long id,
            User user,
            String boardCode,
            String title,
            String content,
            LocalDateTime writeTime,
            LocalDateTime modifyTime,
            Integer view,
            Boolean isDeleted
    ) {
        return new ArticleDto(
                id,
                user,
                boardCode,
                title,
                content,
                writeTime,
                modifyTime,
                view,
                isDeleted
        );
    }

    public static ArticleDto of(Article article) {
        return new ArticleDto(
                article.getId(),
                new User(),
                article.getBoardCode(),
                article.getTitle(),
                article.getContent(),
                article.getWriteTime(),
                article.getModifyTime(),
                article.getView(),
                article.getIsDeleted()
        );
    }

    public Article toEntity(Article article) {
        return Article.of(
                user,
                boardCode,
                title,
                content,
                writeTime,
                modifyTime,
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
