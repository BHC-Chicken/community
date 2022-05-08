package com.peachdevops.community.controller;

import com.peachdevops.community.domain.Article;
import com.peachdevops.community.domain.Comment;
import com.peachdevops.community.domain.User;
import com.peachdevops.community.dto.article.ArticleDto;
import com.peachdevops.community.dto.article.ArticleResponse;
import com.peachdevops.community.dto.article.ArticleViewResponse;
import com.peachdevops.community.dto.comment.CommentResponse;
import com.peachdevops.community.exception.DataAccessErrorException;
import com.peachdevops.community.service.BoardService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
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

import javax.validation.constraints.Size;
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

        boardService.createArticle(article, user);

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
                                    @RequestParam(name = "page") Optional<Integer> parameterPage,
                                    Model model
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

        Page<ArticleDto> articleDtoPage = boardService.getTotalArticles(boardCode, pageable);

        double pageNumber = articleDtoPage.getPageable().getPageNumber();
        double pageSize = articleDtoPage.getPageable().getPageSize();
        int totalPages = articleDtoPage.getTotalPages();
        int startPage = (int) (pageNumber - pageNumber % 10) + 1;
        int tempEndPage = startPage + 9;
        int endPage = Math.min(tempEndPage, totalPages);
        int maxPage = (int) Math.ceil(articleDtoPage.getTotalElements() / 20.0) - 1;

        if (page > maxPage) {
            throw new RuntimeException();
        }

        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("tempEndPage", tempEndPage);
        model.addAttribute("endPage", endPage);

        map.put("articlePage", articleDtoPage);
        map.put("articles", articleList);
        map.put("boardCode", boardCode);

        return new ModelAndView("board/index", map);
    }

    @GetMapping("/{boardCode}/search")
    public ModelAndView searchParam(@PathVariable(name = "boardCode") String boardCode,
                                    @RequestParam(name = "page") Optional<Integer> parameterPage,
                                    @Size(min = 1) String title,
                                    @Size(min = 1) String content,
                                    @Size(min = 1) String nickname,
                                    Model model
    ) {
        int page = parameterPage.orElse(1);
        if (page > 0) {
            page = page - 1;
        }
        final int articleCount = 20;
        Pageable pageable = PageRequest.of(page, articleCount, Sort.by("id").descending());

        String[] contentParam = null;
        if (content != null) {
            contentParam = content.split("\\s+");
        }

        String[] titleParam = null;
        if (title != null) {
            titleParam = title.split("\\s+");
        }

        Map<String, Object> map = new HashMap<>();
        Page<ArticleViewResponse> articles = boardService.getArticleViewResponse(
                titleParam,
                nickname,
                contentParam,
                pageable
        );

        double pageNumber = articles.getPageable().getPageNumber();
        double pageSize = articles.getPageable().getPageSize();
        int totalPages = articles.getTotalPages();
        int startPage = (int) (pageNumber - pageNumber % 10) + 1;
        int tempEndPage = startPage + 9;
        int endPage = Math.min(tempEndPage, totalPages);
        int maxPage = (int) Math.ceil(articles.getTotalElements() / 20.0) - 1;

        if (maxPage < 0) {
            maxPage = 0;
        }
        if (page > maxPage) {
            throw new RuntimeException();
        }

        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("startPage", startPage);
        model.addAttribute("tempEndPage", tempEndPage);
        model.addAttribute("endPage", endPage);

        map.put("articles", articles);
        map.put("articlePage", articles);

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
        Article article1 = new Article();

        Parser parser = Parser.builder().build();
        Node document = parser.parse(article.content());
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        article1.setTitle(article.title());
        article1.setNickname(article.nickname());
        article1.setContent(renderer.render(document));
        article1.setModifyAt(article.modifyAt());
        article1.setView(article.view());
        article1.setBoardCode(article.boardCode());

        map.put("article", article1);

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
