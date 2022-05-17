package com.peachdevops.community.service;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.google.cloud.language.v1.Sentiment;
import com.peachdevops.community.domain.*;
import com.peachdevops.community.dto.article.ArticleDto;
import com.peachdevops.community.dto.article.ArticleViewResponse;
import com.peachdevops.community.dto.comment.CommentDto;
import com.peachdevops.community.exception.DataAccessErrorException;
import com.peachdevops.community.repository.ArticleRepository;
import com.peachdevops.community.repository.BoardsRepository;
import com.peachdevops.community.repository.CommentRepository;
import com.peachdevops.community.repository.RecommendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
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
import java.util.*;

import static com.peachdevops.community.service.TextSentiment.textSentiment;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final BoardsRepository boardsRepository;
    private final RecommendRepository recommendRepository;

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
            String[] title,
            String nickname,
            String[] content,
            Pageable pageable
    ) {

        return articleRepository.findArticleViewPageBySearchParams(
                title, content, nickname, false, pageable);

    }

    private String getSentiment(String text) throws IOException, JSONException {
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
            String responseCode = String.valueOf(urlConnection.getResponseCode());
            System.out.println(returnData);

            return returnData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject createArticle(Article article, User user) throws Exception {

        if (article == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        article.setNickname(user.getNickname());
        String content = article.getContent();
        content = content.replaceAll("(\\r\\n|\\r|\\n|\\n\\r)", " ");
        String result = getSentiment(content);

        JSONObject jsonObject = new JSONObject(result);

        articleRepository.save(article);
        return jsonObject;
    }

    public Map<String, Object> modifyArticle(Long articleId, Article article, User user) throws Exception {
        if (articleId == null || article == null)
            return null;

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
            article2.setModifyAt(LocalDateTime.now());

            Map<String, Object> map = new HashMap<>();
            article.setNickname(user.getNickname());
            String content = article.getContent();
            content = content.replaceAll("(\\r\\n|\\r|\\n|\\n\\r)", " ");
            String result = Arrays.toString(Objects.requireNonNull(getSentiment(content)).getBytes(StandardCharsets.UTF_8));

            map.put("sentiment", result);

            articleRepository.save(article2);
            return map;
        }
        return null;
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
}
