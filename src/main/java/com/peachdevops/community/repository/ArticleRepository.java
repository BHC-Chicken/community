package com.peachdevops.community.repository;

import com.peachdevops.community.domain.Article;
import com.peachdevops.community.domain.QArticle;
import com.peachdevops.community.dto.article.ArticleDto;
import com.peachdevops.community.repository.querydsl.ArticleRepositoryCustom;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.List;


public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        ArticleRepositoryCustom,
        QuerydslPredicateExecutor<Article>,
        QuerydslBinderCustomizer<QArticle> {

    List<ArticleDto> findAllByBoardCodeAndIsDeleted(String boardCode, Boolean is_deleted, Pageable pageable);
    Page<ArticleDto> findByBoardCodeAndIsDeleted(String boardCode, Boolean is_deleted, Pageable pageable);

    int countByTag(String tag);

    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.content, root.nickname, root.tag);
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.nickname).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.tag).first(StringExpression::containsIgnoreCase);
    }
}