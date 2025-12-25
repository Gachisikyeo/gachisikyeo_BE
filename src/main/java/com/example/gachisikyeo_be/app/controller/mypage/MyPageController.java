package com.example.gachisikyeo_be.app.controller.mypage;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchaseStatus;
import com.example.gachisikyeo_be.app.dto.common.SliceResponse;
import com.example.gachisikyeo_be.app.dto.mypage.CompletedGroupPurchaseDetailDto;
import com.example.gachisikyeo_be.app.dto.mypage.MyParticipationGroupPurchaseDto;
import com.example.gachisikyeo_be.app.dto.mypage.MyProfileResponseDto;
import com.example.gachisikyeo_be.app.service.mypage.MypageService;
import com.example.gachisikyeo_be.global.code.ErrorCode;
import com.example.gachisikyeo_be.global.exception.AuthException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Mypage", description = "마이페이지 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MypageService mypageService;

    @Operation(
            summary = "마이페이지 프로필 조회",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/profile")
    public MyProfileResponseDto profile(@AuthenticationPrincipal String userId) {
        Long uid = requireUserId(userId);
        return mypageService.getProfile(uid);
    }

    @Operation(
            summary = "내가 참여한 공구(진행중) 목록",
            description = "마이페이지 진입 시 page=0,size=3으로 호출, 더보기는 page 증가시키며 append",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/participations/ongoing")
    public SliceResponse<MyParticipationGroupPurchaseDto> ongoing(
            @AuthenticationPrincipal String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        Long uid = requireUserId(userId);
        return mypageService.getMyParticipations(uid, GroupPurchaseStatus.OPEN, page, size);
    }

    @Operation(
            summary = "내가 참여한 공구(완료) 목록",
            description = "마이페이지 진입 시 page=0,size=1로 호출(요구사항), 더보기는 size=3으로 page 증가",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/participations/completed")
    public SliceResponse<MyParticipationGroupPurchaseDto> completed(
            @AuthenticationPrincipal String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size
    ) {
        Long uid = requireUserId(userId);
        return mypageService.getMyParticipations(uid, GroupPurchaseStatus.SUCCESS, page, size);
    }

    @Operation(
            summary = "완료된 공구 상세 조회",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/completed/{participationId}")
    public CompletedGroupPurchaseDetailDto completedDetail(
            @PathVariable Long participationId,
            @AuthenticationPrincipal String userId
    ) {
        Long uid = requireUserId(userId);
        return mypageService.getCompletedDetail(uid, participationId);
    }

    private Long requireUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new AuthException(ErrorCode.AUTH_REQUIRED);
        }
        return Long.parseLong(userId);
    }
}
