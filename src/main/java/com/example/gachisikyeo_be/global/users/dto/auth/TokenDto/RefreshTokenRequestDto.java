package com.example.gachisikyeo_be.global.users.dto.auth.TokenDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "refresh 토큰 요청 DTO")
@Getter
@Setter
public class RefreshTokenRequestDto {
    @Schema(description = "refresh 토큰")
    private String refreshToken;
}
