package com.peachdevops.community.controller;

import com.peachdevops.community.domain.User;
import com.peachdevops.community.service.DetectText;
import com.peachdevops.community.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Arrays;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/signup")
    public String getSignUp() {

        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user) throws MessagingException {

        try {
            userService.signup(user.getUsername(), user.getPassword(), user.getNickname());
        } catch (Exception e) {
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
    public String getVerificationEmail(@RequestParam(value = "code") String code) {

        try {
            userService.verificationCode(code);
        } catch (Exception e) {
            return "index";
        }
        return "orcSignup";
    }

    @PostMapping("/verificationEmail")
    public void postOrcSignup(@RequestParam(value = "image", required = false) MultipartFile[] files,
                              @RequestParam(value = "code", required = false) String code) throws IOException{
        userService.uploadImage(files[0], code);
        System.out.println(Arrays.toString(files));
    }
}