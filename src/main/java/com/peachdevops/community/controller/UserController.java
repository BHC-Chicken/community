package com.peachdevops.community.controller;

import com.peachdevops.community.domain.User;
import com.peachdevops.community.dto.UserRegisterDto;
import com.peachdevops.community.exception.AlreadyRegisteredUserException;
import com.peachdevops.community.service.UserService;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

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

        userService.verificationCode(code);

        return "verificationEmail";
    }



}