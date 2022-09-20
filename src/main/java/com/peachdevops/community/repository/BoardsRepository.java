package com.peachdevops.community.repository;

import com.peachdevops.community.domain.Boards;
import com.peachdevops.community.domain.QBoards;
import com.peachdevops.community.repository.querydsl.BoardsRepositoryCustom;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface BoardsRepository extends
        JpaRepository<Boards, String>,
        BoardsRepositoryCustom,
        QuerydslPredicateExecutor<Boards>,
        QuerydslBinderCustomizer<QBoards> {
    Boards findByBoardCode(String boardCode);

    @Override
    default void customize(QuerydslBindings bindings, QBoards root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.boardCode, root.boardName);
        bindings.bind(root.boardName).first(StringExpression::containsIgnoreCase);
    }
}