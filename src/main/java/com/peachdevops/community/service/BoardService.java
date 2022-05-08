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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public List<ArticleDto> getArticles(String boardCode, Pageable pageable) {
        return articleRepository.findAllByBoardCodeAndIsDeleted(boardCode, false, pageable);
    }

    public Page<ArticleDto> getTotalArticles(String boardCode, Pageable pageable) {
        return articleRepository.findByBoardCodeAndIsDeleted(boardCode, false, pageable);
    }

    public Optional<ArticleDto> getArticle(Long articleId) {
        try {
            Optional<Article> article = articleRepository.findById(articleId);
            if (article.isPresent()) {
                Article article1 = article.get();
                article1.increaseViewCount();
                System.out.println(article1.getView());
                articleRepository.save(article1);
            }

            return articleRepository.findById(articleId).map(ArticleDto::of);
        } catch (Exception e) {
            throw new DataAccessErrorException();
        }
    }

    public List<CommentDto> getComments(Long articleId, Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return commentRepository.findAllByArticleIdAndIsDeleted(articleId, false, sort);
    }

    public Page<ArticleViewResponse> getArticleViewResponse(
            String [] title,
            String nickname,
            String [] content,
            Pageable pageable
    ) {

        return articleRepository.findArticleViewPageBySearchParams(
                title, content, nickname, pageable);

    }

    public boolean createArticle(Article article, User user) {

        if (article == null) {
            return false;
        }

        article.setNickname(user.getNickname());

        articleRepository.save(article);
        return true;
    }

    public boolean modifyArticle(Long articleId, Article article, User user) {
        if (articleId == null || article == null)
            return false;

        Optional<Article> article1 = articleRepository.findById(articleId);
        if (article1.isPresent()) {
            Article article2 = article1.get();
            if (!article2.getNickname().equals(user.getNickname())) {
                throw new RuntimeException();
            }
            if (article2.getIsDeleted().equals(true)) {
                throw new RuntimeException();
            }
            article2.setTitle(article.getTitle());
            article2.setContent(article.getContent());

            articleRepository.save(article2);
        }
        return true;
    }

    public boolean deleteArticle(Long articleId, User user) {
        Optional<Article> article = articleRepository.findById(articleId);
        if (article.isPresent()) {
            Article article1 = article.get();
            if (!article1.getNickname().equals(user.getNickname())) {
                throw new RuntimeException();
            }
            article1.setIsDeleted(true);
            articleRepository.save(article1);
        }
        return true;
    }

    public void deleteComment(Long commentId, User user) {

        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent()) {
            Comment comment1 = comment.get();
            if (!comment1.getNickname().equals(user.getNickname())) {
                throw new RuntimeException();
            }
            comment1.setIsDeleted(true);
            commentRepository.save(comment1);
        }
    }


    public boolean putComment(Comment comment) {
        if (comment.getContent() == null) {
            return false;
        }
        commentRepository.save(comment);

        return true;
    }

    public void modifyComment(Long commentId, Comment comment) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            Comment comment1 = commentOptional.get();
            if (!comment1.getNickname().equals(comment.getNickname())) {
                throw new DataAccessErrorException();
            }
            comment1.setContent(comment.getContent());
            commentRepository.save(comment1);
        }
    }

}
