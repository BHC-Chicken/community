package com.peachdevops.community.controller;

import com.peachdevops.community.domain.Article;
import com.peachdevops.community.domain.Comment;
import com.peachdevops.community.domain.User;
import com.peachdevops.community.dto.article.ArticleDto;
import com.peachdevops.community.dto.article.ArticleResponse;
import com.peachdevops.community.dto.comment.CommentResponse;
import com.peachdevops.community.exception.DataAccessErrorException;
import com.peachdevops.community.service.BoardService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @GetMapping("/modify/{boardCode}/{articleId}")
    public String modifyArticle(@PathVariable(name = "boardCode") String boardCode,
                                @PathVariable(name = "articleId") Long articleId,
                                @SessionAttribute(name = "user") User user,
                                Model model) {
        Optional<ArticleDto> article = boardService.getArticle(articleId);
        if (article.isPresent()) {
            ArticleDto article1 = article.get();
            if (!article1.nickname().equals(user.getNickname())) {
                throw new DataAccessErrorException();
            }
            if (article1.isDeleted().equals(true)) {
                throw new DataAccessErrorException();
            }
            model.addAttribute("article", article1);
        } else {
            throw new DataAccessErrorException();
        }

        return "board/modify";
    }

    @PostMapping("/modify/{boardCode}/{articleId}")
    public String modifyArticle(@PathVariable(name = "boardCode") String boardCode,
                                @PathVariable(name = "articleId") Long articleId,
                                @SessionAttribute(name = "user") User user,
                                Article article) {
        boardService.modifyArticle(articleId, article, user);

        return "redirect:/board/" + boardCode;
    }

    @PostMapping("/delete/{boardCode}/{articleId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteArticle(@PathVariable(name = "boardCode") String boardCode,
                              @PathVariable(name = "articleId") Long articleId,
                              @SessionAttribute(name = "user") User user) {
        boardService.deleteArticle(articleId, user);
    }

    @GetMapping("/{boardCode}")
    public ModelAndView articleList(@PathVariable(name = "boardCode") String boardCode,
                                    @RequestParam(name = "page") Optional<Integer> parameterPage
    ) {
        int page = parameterPage.orElse(1);
        if (page > 0) {
            page = page - 1;
        }
        final int articleCount = 20;
        Pageable pageable = PageRequest.of(page, articleCount, Sort.by("id").descending());
        //TODO : 존재하지 않는 게시판에 접근시 예외처리 해야함. boardService 에 getBoard 메서드 작성.
        Map<String, Object> map = new HashMap<>();
        List<ArticleResponse> articleList = boardService.getArticles(boardCode, pageable)
                .stream()
                .map(ArticleResponse::from)
                .toList();
        double totalArticles = boardService.getTotalArticles(boardCode);
        int maxPage = (int) (Math.ceil(totalArticles / articleCount));
        System.out.println(maxPage);
        map.put("maxPage", maxPage);
        map.put("articles", articleList);
        map.put("boardCode", boardCode);

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

        map.put("article", article);

        return new ModelAndView("board/detail", map);
    }

    @GetMapping("/comment/{boardCode}/{articleId}")
    public ModelAndView getComment(@PathVariable(name = "articleId") Long articleId,
                                   @PathVariable String boardCode,
                                   Pageable pageable) {
        Map<String, Object> map = new HashMap<>();
        List<CommentResponse> comments = boardService.getComments(articleId, pageable)
                .stream()
                .map(CommentResponse::from)
                .toList();
        map.put("comments", comments);

        return new ModelAndView("board/detail.comment", map);
    }


    @PostMapping("/{boardCode}/{articleId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void createComment(@PathVariable(name = "boardCode") String boardCode,
                              @PathVariable(name = "articleId") Long articleId,
                              @SessionAttribute(name = "user") User user,
                              Comment comment,
                              Model model) {
        comment.setArticleId(articleId);
        comment.setNickname(user.getNickname());

        boardService.putComment(comment);
        model.addAttribute("boardCode", boardCode);
        model.addAttribute("articleId", articleId);
    }

    @PostMapping("/delete/{boardCode}/{articleId}/{commentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteComment(@PathVariable(name = "articleId") Long articleId,
                              @PathVariable(name = "boardCode") String boardCode,
                              @PathVariable(name = "commentId") Long commentId,
                              Model model,
                              @SessionAttribute(name = "user") User user) {
        boardService.deleteComment(commentId, user);
        model.addAttribute("boardCode", boardCode);
        model.addAttribute("articleId", articleId);
        model.addAttribute("commentId", commentId);
    }

    @PostMapping("/modify/{boardCode}/{articleId}/{commentId}")
    public String modifyComment(@PathVariable(name = "articleId") Long articleId,
                                @PathVariable(name = "boardCode") String boardCode,
                                @PathVariable(name = "commentId") Long commentId,
                                Model model,
                                Comment comment,
                                @SessionAttribute(name = "user") User user) {
        comment.setNickname(user.getNickname());

        boardService.modifyComment(commentId, comment);

        return "redirect:/board/" + boardCode + "/" + articleId;
    }
}
