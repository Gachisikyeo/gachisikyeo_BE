package com.example.gachisikyeo_be.app.controller.participation;

import com.example.gachisikyeo_be.app.dto.participation.CreateParticipationRequestDto;
import com.example.gachisikyeo_be.app.dto.participation.CreateParticipationResponseDto;
import com.example.gachisikyeo_be.app.dto.payment.ParticipationPaymentPageResponseDto;
import com.example.gachisikyeo_be.app.service.participation.ParticipationService;
import com.example.gachisikyeo_be.global.code.ErrorCode;
import com.example.gachisikyeo_be.global.code.SuccessCode;
import com.example.gachisikyeo_be.global.exception.BusinessException;
import com.example.gachisikyeo_be.global.responseTemplate.ApiResponseTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ParticipationCommandController {

    private final ParticipationService participationService;

    // “결제하기” 클릭 시 호출: Participation(PENDING) 생성
    @PostMapping("/group-purchases/{groupPurchaseId}/participations")
    public ResponseEntity<ApiResponseTemplate<CreateParticipationResponseDto>> create(
            @PathVariable Long groupPurchaseId,
            @Valid @RequestBody CreateParticipationRequestDto req,
            Authentication authentication
    ) {
        Long userId = extractUserId(authentication);
        CreateParticipationResponseDto res = participationService.createPending(userId, groupPurchaseId, req);
        return ApiResponseTemplate.success(SuccessCode.PARTICIPATION_CREATED, res);
    }

    /**
     * 결제 화면에서 participationId로 결제 정보 조회
     */
    @GetMapping("/participations/{participationId}")
    public ResponseEntity<ApiResponseTemplate<ParticipationPaymentPageResponseDto>> getPaymentPage(
            @PathVariable Long participationId,
            Authentication authentication
    ) {
        Long userId = extractUserId(authentication);
        ParticipationPaymentPageResponseDto res = participationService.getPaymentPage(userId, participationId);
        return ApiResponseTemplate.success(SuccessCode.PARTICIPATION_PAYMENT_PAGE_FETCHED, res);
    }

    private Long extractUserId(Authentication authentication) {
        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new BusinessException(ErrorCode.AUTH_REQUIRED);
        }
        try {
            return Long.valueOf(authentication.getName());
        } catch (NumberFormatException e) {
            throw new BusinessException(ErrorCode.AUTH_INVALID_PRINCIPAL);
        }
    }
}
