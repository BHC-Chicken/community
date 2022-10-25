package com.peachdevops.community.dto.article;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.peachdevops.community.domain.Article;

import java.io.Serializable;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ArticleDto(
        Long id,
        String nickname,
        String boardCode,
        String title,
        String content,
        String tag,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime writeAt,
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime modifyAt,
        String docsType,
        Integer view,
        Boolean isDeleted,
        Boolean isNotice,
        Long recommendCount,
        String sentiment
) implements Serializable {

    public ArticleDto {
    }

    public static ArticleDto of(
            Long id,
            String nickname,
            String boardCode,
            String title,
            String content,
            String tag,
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
                tag,
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
                article.getTag(),
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
                tag,
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
