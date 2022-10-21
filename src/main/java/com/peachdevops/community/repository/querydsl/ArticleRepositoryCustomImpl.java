package com.peachdevops.community.repository.querydsl;

import com.peachdevops.community.domain.Article;
import com.peachdevops.community.domain.QArticle;
import com.peachdevops.community.dto.article.ArticleViewResponse;
import com.peachdevops.community.exception.DataAccessErrorException;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom {

    private final EntityManager em;

    public ArticleRepositoryCustomImpl(EntityManager em) {
        super(Article.class);
        this.em = em;
    }

    @Override
    public Page<ArticleViewResponse> findArticleViewPageBySearchParams(
            String[] title,
            String[] content,
            String nickname,
            String tag,
            Boolean is_deleted,
            String boardCode,
            Pageable pageable
    ) {
        QArticle article = QArticle.article;

        JPQLQuery<ArticleViewResponse> query = from(article)
                .select(Projections.constructor(
                        ArticleViewResponse.class,
                        article.id,
                        article.boardCode,
                        article.title,
                        article.nickname,
                        article.content,
                        article.tag,
                        article.writeAt,
                        article.modifyAt,
                        article.docsType,
                        article.view,
                        article.isDeleted,
                        article.isNotice,
                        article.recommendCount,
                        article.sentiment
                ));

        if (title != null) {
            for (String word : title) {
                query.where(article.title.contains(word));
            }
        }
        if (content != null) {
            for (String word : content) {
                query.where(article.content.contains(word));
            }
        }
        if (nickname != null && !nickname.isBlank()) {
            query.where(article.nickname.contains(nickname));
        }
        if (tag != null && !tag.isBlank()) {
            tag = tag.replace(" ", "");
            query.where(Expressions.stringTemplate("replace({0},' ','')", article.tag).eq(tag));
        }
        if (!is_deleted) {
            query.where(article.isDeleted.eq(false));
        }
        query.where(article.boardCode.eq(boardCode));

        List<ArticleViewResponse> articles = Optional.ofNullable(getQuerydsl())
                .orElseThrow(DataAccessErrorException::new)
                .applyPagination(pageable, query).fetch();
        return new PageImpl<>(articles, pageable, query.fetchCount());
    }

    @Override
    public List<String> countByTagOrderByDescGroupByTag() {
        QArticle article = QArticle.article;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        return queryFactory.select(article.tag)
                .from(article)
                .groupBy(article.tag)
                .orderBy(article.tag.count().desc())
                .limit(10)
                .fetch();
    }
}
