package com.example.gachisikyeo_be.global.users.dto.auth.TokenDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "토큰 요청 응답 DTO")
@Getter
@Builder
@AllArgsConstructor
public class TokenDto {
    @Schema(description = "액세스 토큰")
    private String accessToken;

    @Schema(description = "refresh 토큰")
    private String refreshToken;
}
