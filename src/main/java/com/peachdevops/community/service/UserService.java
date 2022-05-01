package com.peachdevops.community.service;

import com.peachdevops.community.domain.User;
import com.peachdevops.community.dto.UserRegisterDto;
import com.peachdevops.community.exception.NonExistentCollegeException;
import com.peachdevops.community.exception.NotValidationRegExpException;
import com.peachdevops.community.exception.VerificationCodeAlreadyUsedException;
import com.peachdevops.community.exception.VerificationCodeNotFoundException;
import com.peachdevops.community.repository.RegisterVerificationCodeRepository;
import com.peachdevops.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
            throw new NotValidationRegExpException();
        }
        if (!checkPassword(password)) {
            throw new NotValidationRegExpException();
        }
        if (!checkNickname(nickname)) {
            throw new NotValidationRegExpException();
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

        UserRegisterDto userRegisterDto = registerVerificationCodeRepository.findByVerificationCode(code);
        User user = userRepository.findByUsername(userRegisterDto.getUsername());

        user.setEmailVerifiedFlag(true);
        userRegisterDto.setExpiredFlag(true);
        registerVerificationCodeRepository.save(userRegisterDto);
        userRepository.save(user);
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

    public void uploadImage(MultipartFile file, Principal principal) throws IOException {

        String name = principal.getName();
        String text = detectText(file.getInputStream());
        String college = getUniversity(text);
        System.out.println(college);
        User user = userRepository.findByUsername(name);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<GrantedAuthority> updateAuthorities = new ArrayList<>();

        updateAuthorities.add(new SimpleGrantedAuthority("ROLE_" + college));

        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updateAuthorities);

        SecurityContextHolder.getContext().setAuthentication(newAuth);

        user.setAuthority("ROLE_" + college);
        user.setStdCardVerifiedFlag(true);
        userRepository.save(user);
    }

    private String getUniversity(String input) {
        String[] universities = {"성산교양대학", "인문대학", "법 행정대학", "경영대학", "사회과학대학",
                "과학생명융합대학", "공과대학", "정보통신대학", "조형예술대학", "사범대학", "재활과학대학",
                "간호대학", "AI학부", "미래융합학부", "스포츠레저학과", "한국어교육학부", "글로벌언어문화학부",
                "법학부", "행정학과", "경찰학부", "부동산 지적학과", "군사학과",
                "경영학부", "경제금융학부", "호텔관광경영학부",
                "사회학과", "국제관계학과", "미디어커뮤니케이션학과", " 심리학과", "문헌정보학과", "아동가정복지학과", "청소년상담복지학과", "사회복지학과", "지역사회개발 복지학과",
                "빅데이터학과", "화학생명과학부", "생명환경학부", "동물자원학과", "산림자원학과", "반려동물산업학과", "수리빅데이터학부",
                "건축공학과", "건설시스템공학과", " 환경기술공학과", "기계공학부", "식품공학과", "식품영양학과", "생명공학과", "화힉공학과", "조경학과", "신소재에너지시스템공학부", "기계융복합공학과", "도시 조경학부",
                "전자전기공학부", "컴퓨터정보공학부", "IT융합학과", "메카트로닉스공학과",
                "융합예술학부", "시각디자인융합학부", "산업디자인학과", "패션디자인학과", "실내건축디자인학과",
                "교육과", "교직부",
                "직업재활학과", "치료학과", "재활심리학과", "재료공학과", "재활건강증진학과",
                "간호학과", "보건행정학과",
                "AI소프트웨어", "AI엔터테인먼트",
                "실버복지상담", "자산관리", "평생교육", "청소년학전공", "자유전공"};

        for (String text : universities) {
            if (input.contains(text)) {
                return switch (text) {
                    case "성산교양대학", "자유전공" -> "CULTURE";
                    case "인문대학", "스포츠레저학과", "한국어교육학부", "글로벌언어문화학부", "문화예술학부", "체육학과" -> "HUMAN";
                    case "법 행정대학", "법학부", "행정학과", "경찰학부", "부동산 지적학과", "군사학과" -> "LAW";
                    case "경영대학", "경영학부", "경제금융학부", "호텔관광경영학부" -> "OPERATION";
                    case "사회과학대학", "사회학과", "국제관계학과", "미디어커뮤니케이션학과", " 심리학과", "문헌정보학과", "아동가정복지학과", "청소년상담복지학과", "사회복지학과", "지역사회개발 복지학과" ->
                            "SOCIETY";
                    case "과학생명융합대학", "빅데이터학과", "화학생명과학부", "생명환경학부", "동물자원학과", "산림자원학과", "반려동물산업학과", "수리빅데이터학부" ->
                            "SCIENCE";
                    case "공과대학", "건축공학과", "건설시스템공학과", " 환경기술공학과", "기계공학부", "식품공학과", "식품영양학과", "생명공학과", "화힉공학과", "조경학과", "신소재에너지시스템공학부", "기계융복합공학과", "도시 조경학부" ->
                            "ENGINE";
                    case "정보통신대학", "전자전기공학부", "컴퓨터정보공학부", "IT융합학과", "메카트로닉스공학과" -> "COMPUTER";
                    case "조형예술대학", "융합예술학부", "시각디자인융합학부", "산업디자인학과", "패션디자인학과", "실내건축디자인학과" -> "ART";
                    case "사범대학", "교육과", "교직부" -> "MASTER";
                    case "재활과학대학", "직업재활학과", "치료학과", "재활심리학과", "재료공학과", "재활건강증진학과" -> "REHAB";
                    case "간호대학", "간호학과", "보건행정학과" -> "NURSE";
                    case "AI학부", "AI소프트웨어", "AI엔터테인먼트" -> "AI";
                    case "미래융합학부", "실버복지상담", "자산관리", "평생교육", "청소년학전공" -> "FUSION";
                    default -> "USER";
                };
            }
        }
        throw new NonExistentCollegeException();
    }


}
