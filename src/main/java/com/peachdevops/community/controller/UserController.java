package com.peachdevops.community.controller;

import com.peachdevops.community.domain.User;
import com.peachdevops.community.dto.ModifyPassword;
import com.peachdevops.community.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private boolean checkRole(User user) {
        return user == null;
    }

    @GetMapping("/login")
    public String getLogin(@SessionAttribute(name = "user", required = false) User user,
                           HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !authentication.isAuthenticated()) {
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
    public String signup(@ModelAttribute User user) throws Exception {

        if (!userService.signup(user)) {
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
    @ResponseBody
    public String postOrcSignup(
            @RequestParam(value = "image", required = false) MultipartFile[] files,
            Principal principal,
            HttpSession session,
            @SessionAttribute(name = "user", required = false) User user) throws IOException {
        if (checkRole(user)) {
            return "redirect:/";
        }
        try {
            String college = userService.uploadImage(files[0], principal);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("college", college);
            session.invalidate();

            return jsonObject.toString();
        } catch (Exception e) {
            throw e;
        }
    }

    @GetMapping("/modifyPassword")
    public String getVerificationPassword(@SessionAttribute(name = "user") User user) {
        if (checkRole(user)) {
            return "redirect:/";
        }
        return "user/modifyPassword";
    }

    @PostMapping("/modifyPassword")
    public ResponseEntity<HttpStatus> postVerificationPassword(
            @SessionAttribute(name = "user") User user,
            ModifyPassword modifyPassword,
            HttpSession httpSession,
            User userInfo) {
        userInfo.setUsername(user.getUsername());
        if (userService.verificationPassword(userInfo, modifyPassword)) {
            httpSession.invalidate();
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/searchPassword")
    public String getSearchPassword(
            @SessionAttribute(name = "user", required = false) User user
    ) {
        if (!checkRole(user)) {
            return "redirect:/";
        }
        return "user/searchPassword";
    }

    @PostMapping("/searchPassword")
    public ResponseEntity<HttpStatus> postSearchPassword(User user) throws MessagingException {
        if (userService.findByUsername(user.getUsername()) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.searchPasswordEmailVerification(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/findPassword")
    public String getFindPassword(
            @SessionAttribute(name = "user", required = false) User user,
            @RequestParam(value = "code") String code,
            HttpSession session
    ) {
        if (!checkRole(user)) {
            return "redirect:/";
        }
        try {
            userService.verificationCode(code);
        } catch (Exception e) {
            return "redirect:/";
        }
        session.setAttribute("code", code);
        return "user/findPassword";
    }

    @PostMapping("/findPassword")
    public ResponseEntity<HttpStatus> postFindPassword(
            ModifyPassword modifyPassword,
            @SessionAttribute(name = "code") String code,
            HttpSession session
    ) {
        if (userService.findPassword(code, modifyPassword)) {
            session.removeAttribute("code");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}