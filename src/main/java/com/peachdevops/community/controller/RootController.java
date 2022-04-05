package com.peachdevops.community.controller;

import com.peachdevops.community.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}
