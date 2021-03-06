package com.peachdevops.community.controller;

import com.peachdevops.community.domain.User;
import com.peachdevops.community.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class RootController {

    private final UserService userService;

    @GetMapping("/")
    public String getRoot() {
        return "index";
    }

}
