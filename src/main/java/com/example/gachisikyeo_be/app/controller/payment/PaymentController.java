// src/main/java/com/example/gachisikyeo_be/app/controller/payment/PaymentController.java
package com.example.gachisikyeo_be.app.controller.payment;

import com.example.gachisikyeo_be.app.dto.payment.ConfirmPaymentResponseDto;
import com.example.gachisikyeo_be.app.service.payment.PaymentService;
import com.example.gachisikyeo_be.global.code.ErrorCode;
import com.example.gachisikyeo_be.global.code.SuccessCode;
import com.example.gachisikyeo_be.global.exception.BusinessException;
import com.example.gachisikyeo_be.global.responseTemplate.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name="Payment", description = "결제 상태 정의 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "결제 확정",
    description = "공구 성공 시 결제완료된 상태로 변경됨",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/participations/{participationId}/payments/confirm")
    public ResponseEntity<ApiResponseTemplate<ConfirmPaymentResponseDto>> confirm(
            @PathVariable Long participationId,
            Authentication authentication
    ) {
        Long userId = extractUserId(authentication);
        ConfirmPaymentResponseDto res = paymentService.confirmDummyPayment(userId, participationId);
        return ApiResponseTemplate.success(SuccessCode.PAYMENT_CONFIRMED, res);
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
