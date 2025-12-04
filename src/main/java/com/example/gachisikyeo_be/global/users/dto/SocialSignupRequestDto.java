package com.example.gachisikyeo_be.global.users.dto;

import com.example.gachisikyeo_be.global.users.domain.auth.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialSignupRequestDto {
    private String oauth2SignupToken; // SuccessHandler에서 받은 임시 토큰
    private String nickName;
    private UserType userType;  // SELLER / BUYER
    private Long lawDongId;     // 지역 선택 값
}
