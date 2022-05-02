package com.peachdevops.community.repository.querydsl;

import com.peachdevops.community.domain.User;
import com.peachdevops.community.dto.ArticleDto;
import com.peachdevops.community.dto.ArticleViewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ArticleRepositoryCustom {
    Page<ArticleViewResponse> findArticleViewPageBySearchParams(
            Long id,
            String title,
            User user,
            Pageable pageable
    );

    Optional<ArticleDto> findArticleByBoardCode(
            String boardCode,
            Long articleId
    );

    Optional<ArticleDto> findByBoardCode(
            String boardCode
    );


}
