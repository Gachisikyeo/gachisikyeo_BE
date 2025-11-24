package com.example.gachisikyeo_be.global.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {
    // ✅ Auth
    USER_SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입 성공"), // 201
    USER_LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"), // 200
    USER_LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃 성공"),
    TOKEN_REFRESH_SUCCESS(HttpStatus.OK, "Access Token 재발급 성공"); // 200

    private final HttpStatus httpStatus;
    private final String message;
}
