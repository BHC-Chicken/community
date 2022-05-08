package com.peachdevops.community.repository.querydsl;

import com.peachdevops.community.domain.Article;
import com.peachdevops.community.domain.QArticle;
import com.peachdevops.community.domain.User;
import com.peachdevops.community.dto.article.ArticleViewResponse;
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
    public Page<ArticleViewResponse> findArticleViewPageBySearchParams(
            String [] title,
            String [] content,
            String nickname,
            Pageable pageable
    )
     {
        QArticle article = QArticle.article;

         JPQLQuery<ArticleViewResponse> query = from(article)
                 .select(Projections.constructor(
                         ArticleViewResponse.class,
                         article.id,
                         article.boardCode,
                         article.title,
                         article.nickname,
                         article.content,
                         article.writeAt,
                         article.modifyAt,
                         article.view,
                         article.isDeleted
                 ));

         if (title != null) {
             for (String word:title) {
                 query.where(article.title.contains(word));
             }
         }
         if (content != null) {
             for (String word:content) {
                 query.where(article.content.contains(word));
             }
         }
         if (nickname != null && !nickname.isBlank()) {
             query.where(article.nickname.contains(nickname));
         }

         List<ArticleViewResponse> articles = Optional.ofNullable(getQuerydsl())
                 .orElseThrow(DataAccessErrorException::new)
                 .applyPagination(pageable, query).fetch();
         return new PageImpl<>(articles, pageable, query.fetchCount());
    }
}
