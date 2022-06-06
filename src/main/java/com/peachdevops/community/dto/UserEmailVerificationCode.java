package com.peachdevops.community.dto;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 유저 회원가입용 Dto
 */
@Getter
@Setter
@Entity
public class UserEmailVerificationCode {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String verificationCode;

    @Column
    private boolean expiredFlag;

    @CreatedDate
    @Column(insertable = false, updatable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    @CreatedDate
    @Column(insertable = false, updatable = false)
    private LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(30);

    public UserEmailVerificationCode(String username, String verificationCode) {
        this.username = username;
        this.verificationCode = verificationCode;
    }

    public UserEmailVerificationCode() {
    }
}
