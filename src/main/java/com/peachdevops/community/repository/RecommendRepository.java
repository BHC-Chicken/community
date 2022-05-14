package com.peachdevops.community.repository;

import com.peachdevops.community.domain.Article;
import com.peachdevops.community.domain.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {
    Optional<Recommend> findByArticleIdAndNickname(Long articleId, String nickname);
    void deleteRecommendByArticleIdAndNickname(Long articleId, String nickname);
}
