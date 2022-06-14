package com.peachdevops.community.repository.querydsl;

import com.peachdevops.community.domain.User;
import com.peachdevops.community.dto.article.ArticleViewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {
    Page<ArticleViewResponse> findArticleViewPageBySearchParams(
            String[] title,
            String[] content,
            String nickname,
            Boolean is_deleted,
            String boardCode,
            Pageable pageable
    );

}
