package com.example.gachisikyeo_be.app.dto.payment;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchaseStatus;
import com.example.gachisikyeo_be.app.domain.payment.Payment;
import com.example.gachisikyeo_be.app.domain.payment.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "결제 응답 DTO")
@Getter
@Builder
public class ConfirmPaymentResponseDto {

    @Schema(description = "결제 ID")
    private Long paymentId;

    @Schema(description = "참여자 ID")
    private Long participationId;

    @Schema(description = "공구 ID")
    private Long groupPurchaseId;

    @Schema(description = "결제금액")
    private int amount;

    @Schema(description = "결제 status(PAID, FAILED 등)")
    private PaymentStatus paymentStatus;

    @Schema(description = "현재 주문(참여) 수량")
    private int currentQuantity;

    @Schema(description = "목표 수량")
    private int targetQuantity;

    @Schema(description = "공구 status(OPEN, SUCCESS 등)")
    private GroupPurchaseStatus groupPurchaseStatus;

    @Schema(description = "결제 시간")
    private LocalDateTime paidAt;

    public static ConfirmPaymentResponseDto from(Payment payment) {
        var p = payment.getParticipation();
        var gp = p.getGroupPurchase();
        return ConfirmPaymentResponseDto.builder()
                .paymentId(payment.getId())
                .participationId(p.getId())
                .groupPurchaseId(gp.getId())
                .amount(payment.getAmount())
                .paymentStatus(payment.getStatus())
                .currentQuantity(gp.getCurrentQuantity())
                .targetQuantity(gp.getTargetQuantity())
                .groupPurchaseStatus(gp.getStatus())
                .paidAt(payment.getPaidAt())
                .build();
    }
}
