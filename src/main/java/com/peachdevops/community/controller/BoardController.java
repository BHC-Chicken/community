package com.peachdevops.community.controller;

import com.peachdevops.community.constant.ErrorCode;
import com.peachdevops.community.domain.Article;
import com.peachdevops.community.domain.Comment;
import com.peachdevops.community.domain.Report;
import com.peachdevops.community.domain.User;
import com.peachdevops.community.dto.article.ArticleDto;
import com.peachdevops.community.dto.article.ArticleResponse;
import com.peachdevops.community.dto.article.ArticleViewResponse;
import com.peachdevops.community.dto.comment.CommentResponse;
import com.peachdevops.community.exception.AlreadyDeletedException;
import com.peachdevops.community.exception.DataAccessErrorException;
import com.peachdevops.community.exception.NotFoundBoardException;
import com.peachdevops.community.exception.UserNotFoundException;
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
import org.springframework.http.ResponseEntity;
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

    final int articleCount = 20;
    private final BoardService boardService;

    private boolean checkRole(User user, String boardCode) {
        return user.getAuthorities().stream().noneMatch(r -> r.getAuthority().equals("ROLE_" + boardCode.toUpperCase()));
    }

    @GetMapping("/collegeList")
    private String collegeList() {

        return "board/collegeList";
    }

    private void list(Model model, Map<String, Object> map, String boardCode, int page,
                      String[] titleParam,
                      String nickname,
                      String[] contentParam) {
        Pageable pageable;

        double pageNumber;
        double pageSize;
        int totalPages;
        int maxPage;

        if (titleParam != null || nickname != null || contentParam != null) {
            pageable = PageRequest.of(page, articleCount, Sort.by("id").descending());
            Page<ArticleViewResponse> articleViewResponses = boardService.getArticleViewResponse(
                    titleParam,
                    nickname,
                    contentParam,
                    boardCode,
                    pageable
            );
            if (titleParam != null) {
                map.put("searchType", "title");
                map.put("searchValue", String.join(" ", titleParam));
            } else if (nickname != null) {
                map.put("searchType", "nickname");
                map.put("searchValue", nickname);
            } else {
                map.put("searchType", "content");
                map.put("searchValue", String.join(" ", contentParam));
            }

            pageNumber = articleViewResponses.getPageable().getPageNumber();
            pageSize = articleViewResponses.getPageable().getPageSize();
            totalPages = articleViewResponses.getTotalPages();
            maxPage = (int) Math.ceil(articleViewResponses.getTotalElements() / (float) articleCount) - 1;

            map.put("articles", articleViewResponses);
            map.put("articlePage", articleViewResponses);
        } else {
            pageable = PageRequest.of(page, articleCount, Sort.by("isNotice").descending().and(Sort.by("id").descending()));
            Page<ArticleDto> articleDtoPage = boardService.getTotalArticles(boardCode, pageable);
            List<ArticleResponse> articleList = boardService.getArticles(boardCode, pageable)
                    .stream()
                    .map(ArticleResponse::from)
                    .toList();
            map.put("articles", articleList);

            pageNumber = articleDtoPage.getPageable().getPageNumber();
            pageSize = articleDtoPage.getPageable().getPageSize();
            totalPages = articleDtoPage.getTotalPages();
            maxPage = (int) Math.ceil(articleDtoPage.getTotalElements() / (float) articleCount) - 1;
            map.put("articlePage", articleDtoPage);
        }

        int startPage = (int) (pageNumber - pageNumber % 10) + 1;
        int tempEndPage = startPage + 9;
        int endPage = Math.min(tempEndPage, totalPages);

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

        map.put("boardCode", boardCode);
    }

    @GetMapping("/write/{boardCode}")
    public String createArticleGet(@PathVariable(name = "boardCode") String boardCode,
                                   @SessionAttribute(name = "user", required = false) User user) {
        if (user == null) {
            return "redirect:/board/" + boardCode;
        }
        if (checkRole(user, boardCode)) {
            return "redirect:/board/" + boardCode;
        }
        return "board/write"; // TODO : 에러 발생 시켜서 자바스크립트에서 ajax 로 처리하고 alert 띄워야함.
    }

    @PostMapping("/post/{boardCode}")
    public ResponseEntity<HttpStatus> createArticle(@PathVariable(name = "boardCode") String boardCode,
                                                    @SessionAttribute(name = "user") User user,
                                                    Article article,
                                                    Model model) throws Exception {
        if (boardService.createArticle(article, user) == ErrorCode.OK) {
            model.addAttribute("article", article);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/modify/{boardCode}/{articleId}")
    public String modifyArticle(@PathVariable(name = "boardCode") String boardCode,
                                @PathVariable(name = "articleId") Long articleId,
                                @SessionAttribute(name = "user") User user,
                                Model model) throws Exception {
        if (checkRole(user, boardCode)) {
            return "redirect:/board/" + boardCode + "/" + articleId;
        }
        Optional<ArticleDto> article = boardService.getArticle(articleId);
        if (article.isPresent()) {
            ArticleDto article1 = article.get();
            if (!article1.nickname().equals(user.getNickname())) {
                throw new UserNotFoundException();
            }
            if (article1.isDeleted().equals(true)) {
                throw new AlreadyDeletedException();
            }
            model.addAttribute("article", article1);
        } else {
            throw new Exception();
        }
        return "board/modify";
    }

    @PostMapping("/modify/{boardCode}/{articleId}")
    public ResponseEntity<HttpStatus> modifyArticle(@PathVariable(name = "boardCode") String boardCode,
                                                    @PathVariable(name = "articleId") Long articleId,
                                                    @SessionAttribute(name = "user") User user,
                                                    Article article) throws Exception {
        if (boardService.modifyArticle(articleId, article, user) == ErrorCode.OK) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/delete/{boardCode}/{articleId}")
    @ResponseStatus(value = HttpStatus.OK)
    public String deleteArticle(@PathVariable(name = "boardCode") String boardCode,
                                @PathVariable(name = "articleId") Long articleId,
                                @SessionAttribute(name = "user") User user,
                                Model model) {

        if (checkRole(user, boardCode)) {
            model.addAttribute("exception", ErrorCode.WRONG_ACCESS.getMessage());
            return "redirect:/board/" + boardCode;
        }
        boardService.deleteArticle(articleId, user);
        return "redirect:/board/" + boardCode;
    }

    @GetMapping("/{boardCode}")
    public ModelAndView articleList(@PathVariable(name = "boardCode") String boardCode,
                                    @RequestParam(name = "page") Optional<Integer> parameterPage,
                                    @SessionAttribute(name = "user", required = false) User user,
                                    Model model
    ) {
        boolean authority = false;
        if (!boardService.getBoards(boardCode)) {
            throw new NotFoundBoardException();
        }

        int page = parameterPage.orElse(1);
        if (page > 0) {
            page = page - 1;
        }
        if (user != null && !checkRole(user, boardCode)) {
            authority = true;
        }

        Map<String, Object> map = new HashMap<>();
        list(model, map, boardCode, page, null, null, null);

        map.put("authority",authority);
        map.put("page", page + 1);

        return new ModelAndView("board/list", map);
    }

    @GetMapping("/{boardCode}/search")
    public ModelAndView searchParam(@PathVariable(name = "boardCode") String boardCode,
                                    @RequestParam(name = "page") Optional<Integer> parameterPage,
                                    @Size(min = 1) String title,
                                    @Size(min = 1) String content,
                                    @Size(min = 1) String nickname,
                                    Model model
    ) {
        Map<String, Object> map = new HashMap<>();
        int page = parameterPage.orElse(1);
        if (page > 0) {
            page = page - 1;
        }

        String[] contentParam = null;
        if (content != null) {
            contentParam = content.split("\\s+");
            map.put("searchType", "content");
            map.put("searchValue", content);
        }

        String[] titleParam = null;
        if (title != null) {
            titleParam = title.split("\\s+");
            map.put("searchType", "title");
            map.put("searchValue", title);
        }

        if (nickname != null) {
            map.put("searchType", "nickname");
            map.put("searchValue", nickname);
        }


        list(model, map, boardCode, page, titleParam, nickname, contentParam);
        map.put("page", page + 1);


        return new ModelAndView("board/list", map);
    }

    @GetMapping("/{boardCode}/{articleId}")
    public ModelAndView articleDetail(@PathVariable(name = "boardCode") String boardCode,
                                      @PathVariable(name = "articleId") Long articleId,
                                      @RequestParam(name = "page") Optional<Integer> page,
                                      @Size(min = 1) String title,
                                      @Size(min = 1) String nickname,
                                      @Size(min = 1) String content,
                                      Model model,
                                      @QuerydslPredicate(root = Comment.class) Predicate predicate) throws Exception {
        Map<String, Object> map = new HashMap<>();
        ArticleResponse article = boardService.getArticle(articleId)
                .map(ArticleResponse::from)
                .orElseThrow(DataAccessErrorException::new);
        Article article1 = new Article();
        if (article.isDeleted()) {
            throw new Exception();
        }


        Parser parser = Parser.builder().build();
        Node document = parser.parse(article.content());
        HtmlRenderer renderer = HtmlRenderer.builder().escapeHtml(true).build();

        article1.setId(article.id());
        article1.setTitle(article.title());
        article1.setNickname(article.nickname());
        article1.setContent(renderer.render(document));
        article1.setModifyAt(article.modifyAt());
        article1.setView(article.view());
        article1.setBoardCode(article.boardCode());
        article1.setRecommendCount(article.recommendCount());
        article1.setSentiment(article.sentiment());

        int page1 = page.orElse(1);
        if (page1 > 0) {
            page1 = page1 - 1;
        }

        String[] ifTitle = null;
        if (title != null) {
            ifTitle = title.split("\\s+");
        }

        String[] ifContent = null;
        if (content != null) {
            ifContent = content.split("\\s+");
        }

        String ifNickname = null;
        if (nickname != null) {
            ifNickname = nickname;
        }

        list(model, map, boardCode, page1, ifTitle, ifNickname, ifContent);

        map.put("article", article1);
        map.put("page", page1 + 1);

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
        if (checkRole(user, boardCode)) {
            return;
        }
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
        if (checkRole(user, boardCode)) {
            return;
        }

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

    @PostMapping("/recommendation/{boardCode}/{articleId}")
    @ResponseBody
    public Map<String, Object> recommendation(@PathVariable(name = "articleId") Long articleId,
                                              @PathVariable(name = "boardCode") String boardCode,
                                              @SessionAttribute(name = "user") User user
    ) throws Exception {
        if (checkRole(user, boardCode)) {
            throw new Exception();
        }
        Long hit = boardService.recommendArticle(articleId, user);

        Map<String, Object> map = new HashMap<>();
        map.put("hit", hit);

        return map;
    }

    @PostMapping("/{boardCode}/{articleId}/report")
    public ResponseEntity<HttpStatus> postReport(@PathVariable(name = "articleId") Long articleId,
                                                 @PathVariable(name = "boardCode") String boardCode,
                                                 @SessionAttribute(name = "user") User user,
                                                 Report report
    ) {
        if (boardService.report(articleId, user, report)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}