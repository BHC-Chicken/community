package com.peachdevops.community.repository;

import com.peachdevops.community.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Report findByArticleIdAndUserId(Long articleId, Long userId);
    Long countByArticleId(Long articleId);
}
