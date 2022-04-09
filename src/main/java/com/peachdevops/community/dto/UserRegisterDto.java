package com.peachdevops.community.dto;

import com.peachdevops.community.domain.User;
import lombok.*;

/**
 * 유저 회원가입용 Dto
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserRegisterDto extends User {

}
