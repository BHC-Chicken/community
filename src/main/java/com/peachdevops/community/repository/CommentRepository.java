package com.peachdevops.community.repository;

import com.peachdevops.community.domain.Comment;
import com.peachdevops.community.dto.comment.CommentDto;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface CommentRepository extends
        JpaRepository<Comment, Long>,
        QuerydslPredicateExecutor<Comment> {

    List<CommentDto> findAllByArticleIdAndIsDeleted(Long articleId, Boolean is_deleted, Sort sort);

}
