package com.peachdevops.community.dto;

import com.peachdevops.community.domain.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 유저 회원가입용 Dto
 */
@Getter
@Setter
public class UserRegisterDto extends User {

    @Column
    @Id
    private Long Id;

    @Column
    private String verificationCode;

    @Column
    private String username;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime expiredAt;

    @Column
    private boolean expiredFlag;

    public UserRegisterDto(String verificationCode, String username) {
        this.verificationCode = verificationCode;
        this.username = username;
    }

    public UserRegisterDto() {
    }
}
