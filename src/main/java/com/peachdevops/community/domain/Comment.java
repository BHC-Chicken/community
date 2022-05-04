package com.peachdevops.community.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Entity
public class Comment {

    @Id
    private Long id;

    @Setter
    private String nickname;

    @Setter
    private String content;

    @Setter
    private Long articleId;

    @Setter
    private LocalDateTime writeAt;

    @Setter
    private LocalDateTime modifyAt;

    @Setter
    private Boolean isDeleted;

    protected Comment(
            Long id,
            String nickname,
            String content,
            Long articleId,
            LocalDateTime writeAt,
            LocalDateTime modifyAt,
            Boolean isDeleted) {
        this.id = id;
        this.nickname = nickname;
        this.content = content;
        this.articleId = articleId;
        this.writeAt = writeAt;
        this.modifyAt = modifyAt;
        this.isDeleted = isDeleted;
    }

    protected Comment() {
    }

    public static Comment of(
            Long id,
            String nickname,
            String content,
            Long articleId,
            LocalDateTime writeAt,
            LocalDateTime modifyAt,
            Boolean isDeleted
    ) {
        return new Comment(
                id,
                nickname,
                content,
                articleId,
                writeAt,
                modifyAt,
                isDeleted
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nickname, content, articleId, writeAt, modifyAt, isDeleted);
    }
}
