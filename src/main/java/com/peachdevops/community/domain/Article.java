package com.peachdevops.community.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Entity
public class Article {
    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String nickname;

    @Setter
    @Column(nullable = false)
    private String boardCode;

    @Setter
    @Column(nullable = false)
    private String title;

    @Setter
    @Column(nullable = false)
    private String content;

    @CreatedDate
    @Setter
    @Column(insertable = false, updatable = false)
    private LocalDateTime writeAt = LocalDateTime.now();

    @LastModifiedDate
    @Setter
    @Column(insertable = false, updatable = false)
    private LocalDateTime modifyAt = LocalDateTime.now();

    @Setter
    @Column(nullable = false)
    private Integer view = 0;

    @Setter
    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Setter
    @Column(nullable = false)
    private Boolean isNotice;

    @Setter
    @Column(nullable = false)
    private Long recommendCount;

    public Article() {
    }

    public Article(
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
            Long recommendCount
    ) {
        this.id = id;
        this.nickname = nickname;
        this.boardCode = boardCode;
        this.title = title;
        this.content = content;
        this.writeAt = writeAt;
        this.modifyAt = modifyAt;
        this.view = view;
        this.isDeleted = isDeleted;
        this.isNotice = isNotice;
        this.recommendCount = recommendCount;
    }

    public static Article of(
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
            Long recommendCount
    ) {
        return new Article(
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
                recommendCount
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
        return Objects.hash(id, nickname, boardCode, title, content, writeAt, modifyAt, view, isDeleted,
                isNotice, recommendCount);
    }

    public void increaseViewCount() {
        this.view++;
    }
    public void increaseRecommendCount() {
        this.recommendCount++;
    }
    public void decreaseRecommendCount() {
        this.recommendCount--;
    }
}
