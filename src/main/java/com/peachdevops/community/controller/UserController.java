package com.peachdevops.community.controller;

import com.peachdevops.community.domain.User;
import com.peachdevops.community.dto.SentimentDto;
import com.peachdevops.community.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String getSignUp() {

        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user, Model model) throws MessagingException {

        try {
            userService.signup(user.getUsername(), user.getPassword(), user.getNickname());
        } catch (Exception e) {
            model.addAttribute("exception", e.getMessage());
            System.out.println(e.getMessage());
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
        System.out.println(principal.getName());
        try {
            userService.uploadImage(files[0], principal);
            session.invalidate();
        } catch (Exception e) {
            model.addAttribute("exception", e.getMessage());
        }
        return "index";
    }
}