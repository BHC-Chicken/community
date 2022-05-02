package com.peachdevops.community.repository.querydsl;

import com.peachdevops.community.domain.Article;
import com.peachdevops.community.domain.QArticle;
import com.peachdevops.community.domain.User;
import com.peachdevops.community.dto.ArticleDto;
import com.peachdevops.community.dto.ArticleViewResponse;
import com.peachdevops.community.exception.DataAccessErrorException;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;

public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom {

    public ArticleRepositoryCustomImpl() {
        super(Article.class);
    }

    @Override
    public Optional<ArticleDto> findArticleByBoardCode(String boardCode, Long articleId) {
        return Optional.empty();
    }

    @Override
    public Optional<ArticleDto> findByBoardCode(String boardCode) {
        return Optional.empty();
    }

    @Override
    public Page<ArticleViewResponse> findArticleViewPageBySearchParams(
            Long id,
            String title,
            User user,
            Pageable pageable
    )
     {
        QArticle article = QArticle.article;

         JPQLQuery<ArticleViewResponse> query = from(article)
                 .select(Projections.constructor(
                         ArticleViewResponse.class,
                         article.id,
                         article.title,
                         article.username
                 ));

         if (title != null && !title.isBlank()) {
             query.where(article.title.contains(title));
         }
         if (user.getNickname() != null && !user.getNickname().isBlank()) {
             query.where(article.username.contains(user.getNickname()));
         }

         List<ArticleViewResponse> articles = Optional.ofNullable(getQuerydsl())
                 .orElseThrow(DataAccessErrorException::new)
                 .applyPagination(pageable, query).fetch();
         return new PageImpl<>(articles, pageable, query.fetchCount());
    }
}
