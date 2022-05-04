package com.peachdevops.community.controller;

import com.peachdevops.community.domain.Article;
import com.peachdevops.community.dto.article.ArticleResponse;
import com.peachdevops.community.exception.DataAccessErrorException;
import com.peachdevops.community.service.BoardService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

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
        System.out.println(articleList);

        return new ModelAndView("board/index", map);
    }

    @GetMapping("/{boardCode}/{articleId}")
    public ModelAndView articleDetail(@PathVariable(name = "boardCode") String boardCode,
                                      @PathVariable(name = "articleId") Long articleId)
    {
        Map<String, Object> map = new HashMap<>();
        ArticleResponse article = boardService.getArticle(articleId, boardCode)
                .map(ArticleResponse::from)
                .orElseThrow(DataAccessErrorException::new);

        map.put("article", article);

        return new ModelAndView("board/detail",map);
    }
}
