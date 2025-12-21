package com.example.gachisikyeo_be.app.controller.mypage;

import com.example.gachisikyeo_be.app.dto.mypage.MypageResponseDto;
import com.example.gachisikyeo_be.app.service.mypage.MypageService;
import com.example.gachisikyeo_be.global.code.ErrorCode;
import com.example.gachisikyeo_be.global.exception.AuthException;
import com.example.gachisikyeo_be.global.users.domain.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MypageController {

    private final MypageService mypageService;

    @Operation(summary = "마이페이지 조회",
            description = "기본 마이페이지 창을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/main")
    public MypageResponseDto getMypage(
            @AuthenticationPrincipal String userId
    ) {
        if (userId == null) {
            throw new AuthException(ErrorCode.AUTH_REQUIRED); //인증 필요한 유저
        }

        return mypageService.getMypage(Long.parseLong(userId));
    }
}
