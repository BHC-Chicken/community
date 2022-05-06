package com.peachdevops.community.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String nickname;

    @Setter
    @Column(nullable = false)
    private String content;

    @Setter
    @Column(nullable = false)
    private Long articleId;

    @CreatedDate
    @Column(insertable = false, updatable = false)
    private LocalDateTime writeAt = LocalDateTime.now();

    @LastModifiedDate
    @Column(insertable = false, updatable = false)
    private LocalDateTime modifyAt = LocalDateTime.now();

    @Setter
    @Column(nullable = false)
    private Boolean isDeleted = false;

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
