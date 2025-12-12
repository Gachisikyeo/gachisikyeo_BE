package com.example.gachisikyeo_be.global.users.dto.auth.TokenDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequestDto {
    private String refreshToken;
}
