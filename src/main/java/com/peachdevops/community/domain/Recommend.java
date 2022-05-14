package com.peachdevops.community.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Entity
public class Recommend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private Long articleId;

    @Setter
    @Column(nullable = false)
    private String nickname;

    public Recommend(Long id, Long articleId, String nickname) {
        this.id = id;
        this.articleId = articleId;
        this.nickname = nickname;
    }

    public Recommend() {
    }

    public static Recommend of(
            Long id,
            Long articleId,
            String nickname

    ) {
        return new Recommend(
                id,
                articleId,
                nickname
        );
    }
}
