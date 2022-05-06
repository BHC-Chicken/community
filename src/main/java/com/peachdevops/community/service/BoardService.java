package com.peachdevops.community.service;

import com.peachdevops.community.domain.Article;
import com.peachdevops.community.domain.Comment;
import com.peachdevops.community.domain.User;
import com.peachdevops.community.dto.article.ArticleDto;
import com.peachdevops.community.dto.article.ArticleViewResponse;
import com.peachdevops.community.dto.comment.CommentDto;
import com.peachdevops.community.exception.DataAccessErrorException;
import com.peachdevops.community.repository.ArticleRepository;
import com.peachdevops.community.repository.CommentRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public List<ArticleDto> getArticles(Predicate predicate, String boardCode) {
        try {
            return StreamSupport.stream(articleRepository.findAll(predicate).spliterator(), false)
                    .map(ArticleDto::of)
                    .filter(articleDto -> articleDto.boardCode().equals(boardCode))
                    .filter(articleDto -> articleDto.isDeleted().equals(false))
                    .toList();
        } catch (Exception e) {
            throw new DataAccessErrorException();
        }
    }

    public Optional<ArticleDto> getArticle(Long articleId) {
        try {
            Optional<Article> article = articleRepository.findById(articleId);
            if (article.isPresent()) {
                Article article1 = article.get();
                article1.increaseViewCount();
                articleRepository.save(article1);
            }

            return articleRepository.findById(articleId).map(ArticleDto::of);
        } catch (Exception e) {
            throw new DataAccessErrorException();
        }
    }

    public List<CommentDto> getComments(Predicate predicate, Long articleId) {
        return StreamSupport.stream(commentRepository.findAll(predicate).spliterator(), false)
                .map(CommentDto::of)
                .filter(commentDto -> commentDto.articleId().equals(articleId))
                .filter(commentDto -> commentDto.isDeleted().equals(false))
                .toList();
    }

    public Page<ArticleViewResponse> getArticleViewResponse(
            Long id,
            String title,
            User user,
            Pageable pageable
    ) {
        try {
            return articleRepository.findArticleViewPageBySearchParams(
                    id, title, user, pageable);
        } catch (Exception e) {
            throw new DataAccessErrorException();
        }
    }

    public boolean createArticle(Article article, User user) {

        if (article == null) {
            return false;
        }

        article.setNickname(user.getNickname());

        articleRepository.save(article);
        return true;
    }

    public boolean modifyArticle(Long articleId, ArticleDto articleDto) {
        try {
            if (articleId == null || articleDto == null)
                return false;

            articleRepository.findById(articleId)
                    .ifPresent(article -> articleRepository.save(articleDto.updateEntity(article)));

            return true;
        } catch (Exception e) {
            throw new DataAccessErrorException();
        }
    }

    public boolean removeArticle(Long articleId) {
        try {
            if (articleId == null) {
                return false;
            }

            articleRepository.deleteById(articleId);
            return true;
        } catch (Exception e) {
            throw new DataAccessErrorException();
        }
    }

    public boolean deleteComment(Long commentId, User user) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent()) {
            Comment comment1 = comment.get();
            if (!comment1.getNickname().equals(user.getNickname())) {
                return false;
            }
            comment1.setIsDeleted(true);
            commentRepository.save(comment1);
            return true;
        }
        return false;
    }


    public boolean putComment(Comment comment) {
        if (comment == null) {
            return false;
        }

        commentRepository.save(comment);

        return true;
    }

}
