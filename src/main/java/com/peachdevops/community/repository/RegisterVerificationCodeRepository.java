package com.peachdevops.community.repository;

import com.peachdevops.community.domain.User;
import com.peachdevops.community.dto.UserRegisterDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterVerificationCodeRepository extends JpaRepository<UserRegisterDto, Long> {
    UserRegisterDto findByVerificationCode(String code);


}
