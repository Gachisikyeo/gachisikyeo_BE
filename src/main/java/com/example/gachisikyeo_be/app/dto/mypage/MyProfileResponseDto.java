package com.example.gachisikyeo_be.app.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyProfileResponseDto {
    private final String nickname;
    private final String email;
    private final String lawDong;
    private final String userType;  // "구매자", "사장님"
}
