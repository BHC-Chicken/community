package com.peachdevops.community.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Article implements Serializable {
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

    @Setter
    @Column
    private String tag;

    @CreatedDate
    @Setter
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(insertable = false, updatable = false)
    private LocalDateTime writeAt = LocalDateTime.now();

    @LastModifiedDate
    @Setter
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime modifyAt = LocalDateTime.now();

    @Setter
    @Column(nullable = false)
    private String docsType;

    @Setter
    @Column(nullable = false)
    private Integer view = 0;

    @Setter
    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Setter
    @Column(nullable = false)
    private Boolean isNotice = false;

    @Setter
    @Column(nullable = false)
    private Long recommendCount = 0L;

    @Setter
    @Column(nullable = false)
    private String sentiment = " ";

    public Article() {
    }

    public Article(
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
        this.id = id;
        this.nickname = nickname;
        this.boardCode = boardCode;
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.writeAt = writeAt;
        this.modifyAt = modifyAt;
        this.docsType = docsType;
        this.view = view;
        this.isDeleted = isDeleted;
        this.isNotice = isNotice;
        this.recommendCount = recommendCount;
        this.sentiment = sentiment;
    }

    public static Article of(
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
        return new Article(
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id != null && id.equals(((Article) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nickname, boardCode, title, content, tag, writeAt, modifyAt, docsType, view, isDeleted,
                isNotice, recommendCount, sentiment);
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
