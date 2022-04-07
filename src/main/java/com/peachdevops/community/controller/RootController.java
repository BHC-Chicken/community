package com.peachdevops.community.controller;

import com.peachdevops.community.service.UserRegisterDto;
import com.peachdevops.community.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RootController {

    private final UserService userService;

    @GetMapping("/")
    public String getRoot() {

        return "index";
    }

    @GetMapping("/login")
    public String getLogin() {

        return "login";
    }

    @GetMapping("/signup")
    public String getSignUp() {

        return "signup";
    }

    @PostMapping("/signup")
    public String signup(
            @ModelAttribute UserRegisterDto userDto
    ) {
        userService.signup(userDto.getUsername(), userDto.getPassword());
        // 회원가입 후 로그인 페이지로 이동
        return "redirect:login";
    }

}
