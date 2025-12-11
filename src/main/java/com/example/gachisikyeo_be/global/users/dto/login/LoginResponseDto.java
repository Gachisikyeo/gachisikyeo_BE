package com.example.gachisikyeo_be.global.users.dto.login;

import com.example.gachisikyeo_be.global.users.domain.auth.AuthProvider;
import com.example.gachisikyeo_be.global.users.domain.auth.UserType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResponseDto {
    private String accessToken;
    private String refreshToken;
    private Long id;
    private String email;
    private String name;
    private String role;
    private AuthProvider authProvider;
    private UserType userType;
}
