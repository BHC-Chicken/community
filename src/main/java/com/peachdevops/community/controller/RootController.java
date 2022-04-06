package com.peachdevops.community.controller;

import com.peachdevops.community.domain.User;
import com.peachdevops.community.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/signUp")
    public String getSignUp() {

        return "signUp";
    }

    @PostMapping("/signUp")
    public String postSignUp(User user) {

        userService.saveUser(user);

        return "redirect:/";
    }

}
