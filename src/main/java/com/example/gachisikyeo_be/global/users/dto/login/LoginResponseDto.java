package com.example.gachisikyeo_be.global.users.dto.login;

import com.example.gachisikyeo_be.app.dto.LawDongDto;
import com.example.gachisikyeo_be.global.users.domain.auth.AuthProvider;
import com.example.gachisikyeo_be.global.users.domain.auth.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "로그인 응답 DTO")
@Builder
@Getter
public class LoginResponseDto {
    @Schema(description = "액세스 토큰")
    private String accessToken;

    @Schema(description = "refresh 토큰")
    private String refreshToken;

    @Schema(description = "사용자 ID", example = "1")
    private Long id;

    @Schema(description = "이메일 주소", example = "bee@gmail.com")
    private String email;

    @Schema(description = "본명", example = "호박벌")
    private String name;

    @Schema(description = "닉네임", example = "호호")
    private String nickName;

    //@Schema(description = "권한?", example = "USER")
    private String role;

    @Schema(description = "인증 토큰 발행자(소셜인지 로컬인지)", example = "LOCAL")
    private AuthProvider authProvider;

    @Schema(description = "유저 타입", example = "BUYER")
    private UserType userType;

    @Schema(description = "사용자 법정동 정보")
    private LawDongDto lawDong;
}
