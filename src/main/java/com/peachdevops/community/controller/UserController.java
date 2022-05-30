package com.peachdevops.community.controller;

import com.peachdevops.community.domain.User;
import com.peachdevops.community.service.UserService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/login")
    public String getLogin(@SessionAttribute(name = "user", required = false) User user) {
        if (user != null) {
            return "redirect:/";
        }

        return "login";
    }

    @GetMapping("/signup")
    public String getSignUp(@SessionAttribute(name = "user", required = false) User user) {
        if (user != null) {
            return "redirect:/";
        }
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user, Model model) throws MessagingException {

        try {
            userService.signup(user.getUsername(), user.getPassword(), user.getNickname());
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
        int check = userService.checkInfoUsername(user.getUsername());

        return check;
    }

    @PostMapping("/signup/check-nickname")
    @ResponseBody
    public int checkNickname(@ModelAttribute User user) {
        int check = userService.checkInfoNickname(user.getNickname());

        return check;
    }

    @GetMapping("/verificationEmail")
    public String getVerificationEmail(@RequestParam(value = "code") String code, Model model) {

        try {
            userService.verificationCode(code);
        } catch (Exception e) {
            model.addAttribute("exception", e.getMessage());
            return "verificationEmail";
        }
        return "index";
    }

    @GetMapping("/orcSignup")
    public String getOrcSignup() {
        return "orcSignup";
    }

    @PostMapping("/orcSignup")
    public String postOrcSignup(
            @RequestParam(value = "image", required = false) MultipartFile[] files,
            Principal principal,
            HttpSession session,
            User user,
            Model model) throws IOException {
        try {
            userService.uploadImage(files[0], principal);
            session.invalidate();
        } catch (Exception e) {
            model.addAttribute("exception", e.getMessage());
        }
        return "index";
    }
}