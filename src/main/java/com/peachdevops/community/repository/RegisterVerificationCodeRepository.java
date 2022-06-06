package com.peachdevops.community.repository;

import com.peachdevops.community.dto.UserEmailVerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterVerificationCodeRepository extends JpaRepository<UserEmailVerificationCode, Long> {
    UserEmailVerificationCode findByVerificationCode(String code);


}
