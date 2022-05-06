package com.peachdevops.community.controller;

import com.peachdevops.community.domain.Article;
import com.peachdevops.community.domain.Comment;
import com.peachdevops.community.domain.User;
import com.peachdevops.community.dto.article.ArticleDto;
import com.peachdevops.community.dto.article.ArticleResponse;
import com.peachdevops.community.dto.comment.CommentDto;
import com.peachdevops.community.dto.comment.CommentResponse;
import com.peachdevops.community.exception.DataAccessErrorException;
import com.peachdevops.community.service.BoardService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/write/{boardCode}")
    public String createArticleGet(@PathVariable(name = "boardCode") String boardCode) {

        return "board/write";
    }

    @PostMapping("/post/{boardCode}")
    public String createArticle(@PathVariable(name = "boardCode") String boardCode,
                                @SessionAttribute(name = "user") User user,
                                Article article,
                                Model model
    ) {
        try {
            boardService.createArticle(article, user);
        } catch (Exception e) {

        }
        model.addAttribute("article", article);
        return "redirect:/board/" + boardCode;
    }

    @GetMapping("/{boardCode}")
    public ModelAndView articleList(@PathVariable(name = "boardCode") String boardCode,
                                    @QuerydslPredicate(root = Article.class) Predicate predicate
    ) {
        //TODO : 존재하지 않는 게시판에 접근시 예외처리 해야함. boardService 에 getBoard 메서드 작성.
        Map<String, Object> map = new HashMap<>();
        List<ArticleResponse> articleList = boardService.getArticles(predicate, boardCode)
                .stream()
                .map(ArticleResponse::from)
                .toList();
        map.put("articles", articleList);
        map.put("boardCode", boardCode);
        System.out.println(articleList);

        return new ModelAndView("board/index", map);
    }

    @GetMapping("/{boardCode}/{articleId}")
    public ModelAndView articleDetail(@PathVariable(name = "boardCode") String boardCode,
                                      @PathVariable(name = "articleId") Long articleId,
                                      @QuerydslPredicate(root = Comment.class) Predicate predicate) {
        Map<String, Object> map = new HashMap<>();
        ArticleResponse article = boardService.getArticle(articleId)
                .map(ArticleResponse::from)
                .orElseThrow(DataAccessErrorException::new);

        List<CommentResponse> comments = boardService.getComments(predicate, articleId)
                .stream()
                .map(CommentResponse::from)
                .toList();

        map.put("article", article);
        map.put("comments", comments);
        System.out.println(comments);

        return new ModelAndView("board/detail", map);
    }

    @PostMapping("/{boardCode}/{articleId}")
    public String createComment(@PathVariable(name = "boardCode") String boardCode,
                                @PathVariable(name = "articleId") Long articleId,
                                @SessionAttribute(name = "user") User user,
                                Comment comment,
                                Model model) {
        comment.setArticleId(articleId);
        comment.setNickname(user.getNickname());

        boardService.putComment(comment);
        model.addAttribute("boardCode", boardCode);
        model.addAttribute("articleId", articleId);

        return "redirect:/board/" + boardCode + "/" + articleId;
    }

    @DeleteMapping("/{boardCode}/{articleId}/{commentId}")
    public boolean deleteComment(@PathVariable(name = "boardCode") String boardCode,
                                 @PathVariable(name = "articleId") Long articleId,
                                 @PathVariable(name = "commentId") Long commentId,
                                 @SessionAttribute(name = "user") User user) {
        boardService.deleteComment(commentId , user);

        return true;
    }
}
