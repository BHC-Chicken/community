package com.peachdevops.community.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private Long userId;

    @Setter
    @Column(nullable = false)
    private Long articleId;

    @Setter
    @Column(nullable = false)
    private String reportReason;

    @Setter
    @Column(nullable = false)
    private Boolean isProcess = false;


    @Setter
    @Column(insertable = false, updatable = false)
    private LocalDateTime reportAt = LocalDateTime.now();

    public Report(Long id, Long userId, Long articleId, String reportReason, Boolean isProcess, LocalDateTime reportAt) {
        this.id = id;
        this.userId = userId;
        this.articleId = articleId;
        this.reportReason = reportReason;
        this.isProcess = isProcess;
        this.reportAt = reportAt;
    }

    public Report() {
    }
}
