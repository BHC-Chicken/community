package com.peachdevops.community.service;

import com.peachdevops.community.config.RestPageImpl;
import com.peachdevops.community.constant.ErrorCode;
import com.peachdevops.community.domain.*;
import com.peachdevops.community.dto.article.ArticleDto;
import com.peachdevops.community.dto.article.ArticleViewResponse;
import com.peachdevops.community.dto.comment.CommentDto;
import com.peachdevops.community.exception.DataAccessErrorException;
import com.peachdevops.community.repository.*;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service

@RequiredArgsConstructor
public class BoardService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final BoardsRepository boardsRepository;
    private final RecommendRepository recommendRepository;

    private final ReportRepository reportRepository;

    public List<ArticleDto> getArticles(String boardCode, Pageable pageable) {
        return articleRepository.findAllByBoardCodeAndIsDeleted(boardCode, false, pageable);
    }

    public Page<ArticleDto> getTotalArticles(String boardCode, Pageable pageable) {
        return new RestPageImpl<>(articleRepository.findByBoardCodeAndIsDeleted(boardCode, false, pageable));
    }

    @Cacheable(value = "ArticleDto", key = "#id", cacheManager = "cacheManager", unless = "#id ==''")
    public Optional<ArticleDto> getArticle(Long id) {
        try {
            Optional<Article> article = articleRepository.findById(id);
            if (article.isPresent()) {
                Article article1 = article.get();
                if (article1.getIsDeleted()) {
                    throw new Exception();
                }
                article1.increaseViewCount();
                articleRepository.save(article1);
            }

            return articleRepository.findById(id).map(ArticleDto::of);
        } catch (Exception e) {
            throw new DataAccessErrorException();
        }
    }

    public List<CommentDto> getComments(Long articleId, Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return commentRepository.findAllByArticleIdAndIsDeleted(articleId, false, sort);
    }

    public Page<ArticleViewResponse> getArticleViewResponse(
            String[] title,
            String nickname,
            String[] content,
            String tag,
            String boardCode,
            Pageable pageable
    ) {

        return new RestPageImpl<>(articleRepository.findArticleViewPageBySearchParams(
                title, content, nickname, tag,false, boardCode, pageable));

    }

    private JSONObject getSentiment(String text) throws IOException {
        try {
            URL url = new URL("https://naveropenapi.apigw.ntruss.com/sentiment-analysis/v1/analyze\n");

            String responseData = "";
            String returnData = "";

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("content", text);
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            while ((responseData = bufferedReader.readLine()) != null) {
                stringBuilder.append(responseData);
            }

            returnData = stringBuilder.toString();
            System.out.println(returnData);
            JSONParser parser = new JSONParser();
            JSONObject object = null;

            object = (JSONObject) parser.parse(returnData);

            return object;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Transactional
    public ErrorCode createArticle(Article article, User user) throws Exception {
        if (article.getTitle().isEmpty() || article.getContent().isEmpty()) {
            return ErrorCode.BAD_REQUEST;
        }
        article.setNickname(user.getNickname());
        String content = article.getContent();
        if (content.length() > 1001) {
            return ErrorCode.BAD_REQUEST;
        }
        content = content.replaceAll("(\\r\\n|\\r|\\n|\\n\\r)", " ");
        JSONObject result = getSentiment(content); // naver api 요청
        JSONArray sentences = (JSONArray) result.get("sentences");
        JSONObject sentencesArray = (JSONObject) sentences.get(0);
        String sentiment = (String) sentencesArray.get("sentiment");

        article.setSentiment(sentiment);
        articleRepository.save(article);

        return ErrorCode.OK;
    }

    @CacheEvict(value = "ArticleDto", key = "#id", cacheManager = "cacheManager")
    public ErrorCode modifyArticle(Long id, Article article, User user) throws Exception {
        if (article.getTitle().isEmpty() || article.getContent().isEmpty()) {
            return ErrorCode.BAD_REQUEST;
        }

        Optional<Article> article1 = articleRepository.findById(id);
        if (article1.isPresent()) {
            Article article2 = article1.get();
            if (!article2.getNickname().equals(user.getNickname())) {
                System.out.println();
                return ErrorCode.BAD_REQUEST;
            }
            if (article2.getIsDeleted().equals(true)) {
                return ErrorCode.BAD_REQUEST;
            }
            article2.setTitle(article.getTitle());
            article2.setContent(article.getContent());
            article2.setDocsType(article.getDocsType());
            article2.setModifyAt(LocalDateTime.now());
            article2.setTag(article.getTag());

            String content = article.getContent();

            if (content.length() > 1001) {
                return ErrorCode.BAD_REQUEST;
            }

            content = content.replaceAll("(\\r\\n|\\r|\\n|\\n\\r)", " ");
            JSONObject result = getSentiment(content);
            JSONArray sentences = (JSONArray) result.get("sentences");
            JSONObject sentencesArray = (JSONObject) sentences.get(0);
            String sentiment = (String) sentencesArray.get("sentiment");
            article2.setSentiment(sentiment);

            articleRepository.save(article2);

            return ErrorCode.OK;
        }

        return ErrorCode.BAD_REQUEST;
    }

    @CacheEvict(value = "ArticleDto", key = "#id", cacheManager = "cacheManager")
    public boolean deleteArticle(Long id, User user) {
        Optional<Article> article = articleRepository.findById(id);
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
        if (comment.getContent().isBlank()) {
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

    public boolean getBoards(String boardCode) {

        Boards boards = boardsRepository.findByBoardCode(boardCode);
        return boards != null;
    }

    @Transactional
    public Long recommendArticle(Long articleId, User user) {
        Optional<Article> article = articleRepository.findById(articleId);
        Optional<Recommend> recommend = recommendRepository.findByArticleIdAndNickname(articleId, user.getNickname());

        if (article.isPresent()) {
            Article article1 = article.get();
            if (recommend.isPresent()) {
                article1.decreaseRecommendCount();
                articleRepository.save(article1);
                recommendRepository.deleteRecommendByArticleIdAndNickname(articleId, user.getNickname());
            } else {
                Recommend recommend1 = new Recommend();
                article1.increaseRecommendCount();
                recommend1.setArticleId(articleId);
                recommend1.setNickname(user.getNickname());
                articleRepository.save(article1);
                recommendRepository.save(recommend1);
            }
            return article1.getRecommendCount();

        } else {
            throw new DataAccessErrorException();
        }
    }

    public boolean report(Long articleId, User user, Report report) {
        report.setArticleId(articleId);
        report.setUserId(user.getId());
        if (reportRepository.findByArticleIdAndUserId(articleId, user.getId()) != null) {
            return false;
        }
        reportRepository.save(report);
        if (reportRepository.countByArticleId(articleId) >= 10) {
            Optional<Article> article = articleRepository.findById(articleId);
            if (article.isPresent()) {
                Article article1 = article.get();
                article1.setIsDeleted(true);
                articleRepository.save(article1);
            }
        }
        return true;
    }

    public List<String> tagList(String boardCode) {
        return articleRepository.countByTagOrderByDescGroupByTag(boardCode);
    }
}
