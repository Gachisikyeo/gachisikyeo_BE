package com.example.gachisikyeo_be.global.users.controller;

import com.example.gachisikyeo_be.global.code.SuccessCode;
import com.example.gachisikyeo_be.global.responseTemplate.ApiResponseTemplate;
import com.example.gachisikyeo_be.global.users.dto.LoginResponseDto;
import com.example.gachisikyeo_be.global.users.dto.SocialSignupRequestDto;
import com.example.gachisikyeo_be.global.users.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("oauth2/signup")
    public ResponseEntity<ApiResponseTemplate<LoginResponseDto>> socialSignup(
            @RequestBody SocialSignupRequestDto socialSignupRequestDto
            ){
        LoginResponseDto loginResponseDto = authService.socialSignup(socialSignupRequestDto);
        return ApiResponseTemplate.success(SuccessCode.USER_SIGNUP_SUCCESS, loginResponseDto);
    }
}
