package com.peachdevops.community.controller;

import com.peachdevops.community.domain.User;
import com.peachdevops.community.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private boolean checkRole(User user) {
        if (user == null) {
            return true;
        }
        return user.getAuthorities().stream().noneMatch(r -> {
            String authority = r.getAuthority();
            if (authority == null) {
                return false;
            }
            return authority.equals("ROLE_USER");
        });
    }

    @GetMapping("/login")
    public String getLogin(@SessionAttribute(name = "user", required = false) User user) {
        if (user != null) {
            return "redirect:/";
        }

        return "user/login";
    }

    @GetMapping("/signup")
    public String getSignUp(@SessionAttribute(name = "user", required = false) User user) {
        if (user != null) {
            return "redirect:/";
        }
        return "user/signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user, Model model) throws MessagingException {

        try {
            userService.signup(user);
        } catch (Exception e) {
            model.addAttribute("exception", e.getMessage());
            return "redirect:signup";
        }
        // 회원가입 후 로그인 페이지로 이동
        return "redirect:login";
    }

    @PostMapping("/signup/check-username")
    @ResponseBody
    public int checkUsername(@ModelAttribute User user) {

        return userService.checkInfoUsername(user.getUsername());
    }

    @PostMapping("/signup/check-nickname")
    @ResponseBody
    public int checkNickname(@ModelAttribute User user) {

        return userService.checkInfoNickname(user.getNickname());
    }

    @GetMapping("/verificationEmail")
    public String getVerificationEmail(@RequestParam(value = "code") String code, Model model) {

        try {
            userService.verificationCode(code);
        } catch (Exception e) {
            model.addAttribute("exception", e.getMessage());
            return "user/verificationEmail";
        }
        return "index";
    }

    @GetMapping("/orcSignup")
    public String getOrcSignup(@SessionAttribute(name = "user", required = false) User user) {
        if (checkRole(user)) {
            return "redirect:/";
        }
        return "user/orcSignup";
    }

    @PostMapping("/orcSignup")
    public String postOrcSignup(
            @RequestParam(value = "image", required = false) MultipartFile[] files,
            Principal principal,
            HttpSession session,
            @SessionAttribute(name = "user", required = false) User user,
            Model model) throws IOException {
        if (checkRole(user)) {
            return "redirect:/";
        }
        try {
            userService.uploadImage(files[0], principal);
            session.invalidate();
        } catch (Exception e) {
            model.addAttribute("exception", e.getMessage());
        }
        return "index";
    }

    @GetMapping("/verificationPassword")
    public String getVerificationPassword(@SessionAttribute(name = "user", required = false) User user) {
        if (checkRole(user)) {
            return "redirect:/";
        }

        return "user/verificationPassword";
    }

    @PostMapping("/verificationPassword")
    public ResponseEntity<HttpStatus> postVerificationPassword(
            @SessionAttribute(name = "user", required = false) User user,
            User userInfo) throws Exception {
        userInfo.setUsername(user.getUsername());
        if (userService.verificationPassword(userInfo)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/modifyPassword")
    public String getModifyPassword(@SessionAttribute(name = "user", required = false) User user) {
        if (checkRole(user)) {
            return "redirect:/";
        }
        return "user/modifyPassword";
    }
}