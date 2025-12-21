package com.example.gachisikyeo_be.app.dto.mypage;

import com.example.gachisikyeo_be.app.domain.region.LawDong;
import com.example.gachisikyeo_be.global.users.domain.auth.UserType;
import lombok.Getter;

@Getter
public class MypageResponseDto {

    private final String nickname;
    private final String email;
    private final String lawDong;
    private final UserType userType;

    public MypageResponseDto(String nickname, String email, String lawDong, UserType userType) {
        this.nickname = nickname;
        this.email = email;
        this.lawDong=lawDong;
        this.userType=userType;
    }
}
