package com.example.gachisikyeo_be.app.controller.mypage;

import com.example.gachisikyeo_be.app.dto.mypage.CompletedGroupPurchaseDetailDto;
import com.example.gachisikyeo_be.app.dto.mypage.MypageResponseDto;
import com.example.gachisikyeo_be.app.service.mypage.MypageService;
import com.example.gachisikyeo_be.global.code.ErrorCode;
import com.example.gachisikyeo_be.global.exception.AuthException;
import com.example.gachisikyeo_be.global.users.domain.auth.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MypageController {

    private final MypageService mypageService;

    @Operation(summary = "마이페이지 조회",
            description = """
            기본 마이페이지 창을 조회합니다.
            조회 내용: 닉네임, 이메일, 주소(법정동), 유저 타입(구매자, 사장님),
            내가 참여한 공구 중 완료된 공구,
            참여한 공구 중 아직 완료 안된 공구
            """,
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/main")
    public MypageResponseDto getMypage( //마이페이지의 메인화면
            @AuthenticationPrincipal String userId,
                                        @RequestParam(defaultValue = "0") int completedPage,
                                        @RequestParam(defaultValue = "0") int ongoingPage
    ) {
        if (userId == null) {
            throw new AuthException(ErrorCode.AUTH_REQUIRED); //인증 필요한 유저
        }

        Pageable completedPg = PageRequest.of(completedPage, 1);
        Pageable ongoingPg = PageRequest.of(ongoingPage, 3);

        return mypageService.getMypage(
                Long.parseLong(userId),
                completedPg,
                ongoingPg
        ); //완료된 공구 조회랑 진행중인 공구(상품 등록 시 입력한 내용 가져오면 될 듯?) 조회도 같이 조회
    }

    //완료된 공구 상세페이지(클릭한 공구 정보 그대로+공구ID,내 닉네임, 총대 닉네임, 내 결제금액 조회)
    @Operation(summary = "완료된 공구 상세페이지 조회",
            description = """
                    완료된 공구 눌렀을 때 뜨는 상세페이지를 조회합니다.
                    조회 내용: 공구 정보, 공구ID(주문번호), 유저 닉네임(구매자명), 총대 닉네임, 내 결제 금액
                    """,
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/completed/{participationId}")
    public CompletedGroupPurchaseDetailDto getCompletedDetail(
            @PathVariable Long participationId,
            @AuthenticationPrincipal String userId
    ) {
        if (userId == null) {
            throw new AuthException(ErrorCode.AUTH_REQUIRED);
        }
        return mypageService.getCompletedDetail(
                Long.parseLong(userId),
                participationId
        );
    }
}
