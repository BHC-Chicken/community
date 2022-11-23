package com.peachdevops.community.repository.querydsl;

import com.peachdevops.community.dto.article.ArticleViewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleRepositoryCustom {
    Page<ArticleViewResponse> findArticleViewPageBySearchParams(
            String[] title,
            String[] content,
            String nickname,
            String tag,
            Boolean is_deleted,
            String boardCode,
            Pageable pageable
    );

    List<String> countByTagOrderByDescGroupByTag(String boardCode);

}
