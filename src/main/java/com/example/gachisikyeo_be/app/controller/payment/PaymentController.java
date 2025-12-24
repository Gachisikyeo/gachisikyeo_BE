// src/main/java/com/example/gachisikyeo_be/app/controller/payment/PaymentController.java
package com.example.gachisikyeo_be.app.controller.payment;

import com.example.gachisikyeo_be.app.dto.payment.ConfirmPaymentResponseDto;
import com.example.gachisikyeo_be.app.service.payment.PaymentService;
import com.example.gachisikyeo_be.global.code.ErrorCode;
import com.example.gachisikyeo_be.global.code.SuccessCode;
import com.example.gachisikyeo_be.global.exception.BusinessException;
import com.example.gachisikyeo_be.global.responseTemplate.ApiResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PaymentController {

    private final PaymentService paymentService;

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
