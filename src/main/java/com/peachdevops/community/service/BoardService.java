package com.peachdevops.community.service;

import com.peachdevops.community.domain.Article;
import com.peachdevops.community.domain.User;
import com.peachdevops.community.dto.ArticleDto;
import com.peachdevops.community.dto.ArticleViewResponse;
import com.peachdevops.community.exception.DataAccessErrorException;
import com.peachdevops.community.repository.ArticleRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.peachdevops.community.domain.QArticle.article;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final ArticleRepository articleRepository;

    public List<ArticleDto> getArticles(Predicate predicate, String boardCode) {
        try {
            return StreamSupport.stream(articleRepository.findAll(predicate).spliterator(), false)
                    .map(ArticleDto::of)
                    .filter(articleDto -> articleDto.boardCode().equals(boardCode))
                    .toList();
        } catch (Exception e) {
            throw new DataAccessErrorException();
        }
    }

    public Optional<ArticleDto> getArticle(Long articleId, String boardCode) {
        try {
            return articleRepository.findById(articleId).map(ArticleDto::of)
                    .filter(articleDto -> articleDto.boardCode().equals(boardCode));
        } catch (Exception e) {
            throw new DataAccessErrorException();
        }
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

    public boolean createArticle(ArticleDto articleDto) {
        try {
            if (articleDto == null) {
                return false;
            }
            Article article = articleRepository.findById(articleDto.id())
                            .orElseThrow(DataAccessErrorException::new);
            articleRepository.save(articleDto.toEntity(article));
            return true;
        } catch (Exception e) {
            throw new DataAccessErrorException();
        }
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

}
