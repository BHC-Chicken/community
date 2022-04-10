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
@Entity
public class UserRegisterDto {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String verificationCode;

    @Column
    private boolean expiredFlag;

    public UserRegisterDto(String username, String verificationCode) {
        this.username = username;
        this.verificationCode = verificationCode;
    }

    public UserRegisterDto() {
    }
}
