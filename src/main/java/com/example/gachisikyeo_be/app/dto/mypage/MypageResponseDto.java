package com.example.gachisikyeo_be.app.dto.mypage;

import com.example.gachisikyeo_be.app.domain.region.LawDong;
import com.example.gachisikyeo_be.global.users.domain.auth.UserType;

public class MypageResponseDto {

    private String nickname;
    private String email;
    private String lawDong;
    private UserType userType;

    public MypageResponseDto(String nickname, String email, String lawDong, UserType userType) {
        this.nickname = nickname;
        this.email = email;
        this.lawDong=lawDong;
        this.userType=userType;
    }
}
