package com.peachdevops.community.service;

import com.peachdevops.community.domain.User;
import com.peachdevops.community.dto.UserRegisterDto;
import com.peachdevops.community.exception.AlreadyRegisteredUserException;
import com.peachdevops.community.exception.VerificationCodeAlreadyUsedException;
import com.peachdevops.community.exception.VerificationCodeNotFoundException;
import com.peachdevops.community.repository.RegisterVerificationCodeRepository;
import com.peachdevops.community.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RegisterVerificationCodeRepository registerVerificationCodeRepository;
    private final PasswordEncoder passwordEncoder;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void signup(
            String username,
            String password
    ) {
        if (userRepository.findByUsername(username) != null) {
            throw new AlreadyRegisteredUserException();
        }
        userRepository.save(new User(username, passwordEncoder.encode(password), "ROLE_USER"));
        String code = passwordEncoder.encode(username);
        registerVerificationCodeRepository.save(new UserRegisterDto(username, code));
    }

    public void verificationCode(String code){
        if (registerVerificationCodeRepository.findByVerificationCode(code) == null) {
            throw new VerificationCodeNotFoundException();
        }

        if (registerVerificationCodeRepository.findByVerificationCode(code).isExpiredFlag()) {
            throw new VerificationCodeAlreadyUsedException();
        }

        registerVerificationCodeRepository.findByVerificationCode(code).setExpiredFlag(true);
    }
}
