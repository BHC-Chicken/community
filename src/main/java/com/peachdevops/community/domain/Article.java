package com.peachdevops.community.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Entity
public class Article {
    @Id
    private Long id;

    private String username;

    @Setter
    private String boardCode;

    @Setter
    private String title;

    @Setter
    private String content;

    @Setter
    private LocalDateTime createdAt;

    @Setter
    private LocalDateTime modifyAt;

    @Setter
    private Integer view;

    @Setter
    private Boolean isDeleted;

    protected Article() {}

    protected Article(
            String username,
            String boardCode,
            String title,
            String content,
            LocalDateTime createdAt,
            LocalDateTime modifyAt,
            Integer view,
            Boolean isDeleted
    ) {
        this.username = username;
        this.boardCode = boardCode;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifyAt = modifyAt;
        this.view = view;
        this.isDeleted = isDeleted;
    }

    public static Article of(
            String username,
            String boardCode,
            String title,
            String content,
            LocalDateTime createdAt,
            LocalDateTime modifyAt,
            Integer view,
            Boolean isDeleted
    ) {
        return new Article(
                username,
                boardCode,
                title,
                content,
                createdAt,
                modifyAt,
                view,
                isDeleted
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id != null && id.equals(((Article) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, boardCode, title, content, createdAt, modifyAt, view, isDeleted);
    }
}
