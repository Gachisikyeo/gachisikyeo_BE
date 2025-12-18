package com.example.gachisikyeo_be.global.users.dto;

import com.example.gachisikyeo_be.global.users.domain.auth.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "소셜 로그인 요청 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SocialSignupRequestDto {
    @Schema(description = "소셜 로그인용 임시 토큰")
    private String oauth2SignupToken; // SuccessHandler에서 받은 임시 토큰

    @Schema(description = "닉네임", example = "꿀벌")
    private String nickName;

    @Schema(description = "유저 타입", example = "BUYER")
    private UserType userType;  // SELLER / BUYER

    @Schema(description = "법정동 ID(지역 선택 값)", example = "1")
    private Long lawDongId;     // 지역 선택 값
}
