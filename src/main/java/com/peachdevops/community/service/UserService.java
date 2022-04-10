package com.peachdevops.community.service;

import com.peachdevops.community.domain.User;
import com.peachdevops.community.dto.UserRegisterDto;
import com.peachdevops.community.exception.AlreadyRegisteredUserException;
import com.peachdevops.community.exception.VerificationCodeAlreadyUsedException;
import com.peachdevops.community.exception.VerificationCodeNotFoundException;
import com.peachdevops.community.repository.RegisterVerificationCodeRepository;
import com.peachdevops.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RegisterVerificationCodeRepository registerVerificationCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void signup(
            String username,
            String password
    ) throws MessagingException {
        if (userRepository.findByUsername(username) != null) {
            throw new AlreadyRegisteredUserException();
        }
        userRepository.save(new User(username, passwordEncoder.encode(password), "ROLE_USER"));
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

        String username = registerVerificationCodeRepository.findByVerificationCode(code).getUsername();
        System.out.println(username);
        User user = userRepository.findByUsername(username);
        user.setEmailVerifiedFlag(true);
        UserRegisterDto userRegisterDto = registerVerificationCodeRepository.findByVerificationCode(code);
        userRegisterDto.setExpiredFlag(true);

        registerVerificationCodeRepository.save(userRegisterDto);
        userRepository.save(user);
    }
}
