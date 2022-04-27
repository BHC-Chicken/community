package com.peachdevops.community.service;

import com.peachdevops.community.domain.User;
import com.peachdevops.community.dto.UserRegisterDto;
import com.peachdevops.community.exception.NonExistentCollegeException;
import com.peachdevops.community.exception.NotValidationRegExp;
import com.peachdevops.community.exception.VerificationCodeAlreadyUsedException;
import com.peachdevops.community.exception.VerificationCodeNotFoundException;
import com.peachdevops.community.repository.RegisterVerificationCodeRepository;
import com.peachdevops.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDateTime;

import static com.peachdevops.community.service.DetectText.detectText;

@Service
@RequiredArgsConstructor
public class UserService {
    public static class RegExp {
        public static final String USERNAME = "^(?=.{8,50}$)([0-9a-z_]{4,})@([0-9a-z][0-9a-z\\-]*[0-9a-z]\\.)?([0-9a-z][0-9a-z\\-]*[0-9a-z])\\.([a-z]{2,15})(\\.[a-z]{2})?$";
        public static final String PASSWORD = "^([0-9a-zA-Z`~!@#$%^&*()\\-_=+\\[{\\]}\\\\|;:'\",<.>/?]{8,50})$";
        public static final String NICKNAME = "^([0-9a-zA-Z가-힣]{1,10})$";

        private RegExp() {

        }
    }

    public static boolean checkUsername(String s) {
        return s != null && s.matches(RegExp.USERNAME);
    }

    public static boolean checkPassword(String s) {
        return s != null && s.matches(RegExp.PASSWORD);
    }

    public static boolean checkNickname(String s) {
        return s != null && s.matches(RegExp.NICKNAME);
    }

    private final UserRepository userRepository;
    private final RegisterVerificationCodeRepository registerVerificationCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void signup(
            String username,
            String password,
            String nickname
    ) throws MessagingException {
        if (!checkUsername(username)) {
            throw new NotValidationRegExp();
        }
        if (!checkPassword(password)) {
            throw new NotValidationRegExp();
        }
        if (!checkNickname(nickname)) {
            throw new NotValidationRegExp();
        }
        userRepository.save(new User(username, passwordEncoder.encode(password), nickname, "ROLE_USER"));
        String code = passwordEncoder.encode(username);
        registerVerificationCodeRepository.save(new UserRegisterDto(username, code));

        MimeMessage message = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(username);
        helper.setSubject("인증");
        helper.setText(String.format("<a href=\"http://127.0.0.1/verificationEmail?code=%s\" target=\"_blank\">인증하기</a>", code), true);

        this.javaMailSender.send(message);
    }

    public void verificationCode(String code) {
        if (registerVerificationCodeRepository.findByVerificationCode(code) == null) {
            throw new VerificationCodeNotFoundException();
        }

        if (registerVerificationCodeRepository.findByVerificationCode(code).isExpiredFlag()) {
            throw new VerificationCodeAlreadyUsedException();
        }

        LocalDateTime expiredAt = registerVerificationCodeRepository.findByVerificationCode(code).getExpiredAt();
        if (LocalDateTime.now().isAfter(expiredAt)) {
            throw new VerificationCodeNotFoundException();
        }
    }

    public int checkInfoUsername(String username) {
        if (userRepository.findByUsername(username) != null) {
            return 0;
        }
        return 1;
    }

    public int checkInfoNickname(String nickname) {
        if (userRepository.findByNickname(nickname) != null) {
            return 0;
        }
        return 1;
    }

    public void uploadImage(MultipartFile file, String code) throws IOException {
        UserRegisterDto userRegisterDto = registerVerificationCodeRepository.findByVerificationCode(code);

        String text = detectText(file.getInputStream());
        String college = getUniversity(text);
        System.out.println(college);
        User user = userRepository.findByUsername(userRegisterDto.getUsername());
        user.setAuthority("ROLE_" + college);
        user.setEmailVerifiedFlag(true);
        userRegisterDto.setExpiredFlag(true);
        registerVerificationCodeRepository.save(userRegisterDto);
        userRepository.save(user);
    }

    private String getUniversity(String input) {
        String[] universities = {"성산교양대학", "인문대학", "법 행정대학", "경영대학", "사회과학대학",
                "과학생명융합대학", "공과대학", "정보통신대학", "조형예술대학", "사범대학", "재활과학대학",
                "간호대학", "AI학부", "미래융합학부"};

        for (String text : universities) {
            if (input.contains(text)) {
                return switch (text) {
                    case "성산교양대학" -> "CULTURE";
                    case "인문대학" -> "HUMAN";
                    case "법 행정대학" -> "ROW";
                    case "경영대학" -> "OPERATION";
                    case "사회과학대학" -> "SOCIETY";
                    case "과학생명융합대학" -> "SCIENCE";
                    case "공과대학" -> "ENGINE";
                    case "정보통신대학" -> "COMPUTER";
                    case "조형예술대학" -> "ART";
                    case "사범대학" -> "MASTER";
                    case "재활과학대학" -> "REHAB";
                    case "간호대학" -> "NURSE";
                    case "AI학부" -> "AI";
                    case "미래융합학부" -> "FUSION";
                    default -> "USER";
                };
            }
        }
        throw new NonExistentCollegeException();
    }
}
