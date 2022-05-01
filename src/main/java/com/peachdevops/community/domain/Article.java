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
    @ManyToOne
    private User user;

    public void setUser(User user) {
        this.user = user;
    }
    @Setter
    private String boardCode;

    @Setter
    private String title;

    @Setter
    private String content;

    @Setter
    private LocalDateTime writeTime;

    @Setter
    private LocalDateTime modifyTime;

    @Setter
    private Integer view;

    @Setter
    private Boolean isDeleted;

    protected Article() {}

    protected Article(
            User user,
            String boardCode,
            String title,
            String content,
            LocalDateTime writeTime,
            LocalDateTime modifyTime,
            Integer view,
            Boolean isDeleted
    ) {
        this.user = user;
        this.boardCode = boardCode;
        this.title = title;
        this.content = content;
        this.writeTime = writeTime;
        this.modifyTime = modifyTime;
        this.view = view;
        this.isDeleted = isDeleted;
    }

    public static Article of(
            User user,
            String boardCode,
            String title,
            String content,
            LocalDateTime writeTime,
            LocalDateTime modifyTime,
            Integer view,
            Boolean isDeleted
    ) {
        return new Article(
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id != null && id.equals(((Article) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, boardCode, title, content, writeTime, modifyTime, view, isDeleted);
    }
}
