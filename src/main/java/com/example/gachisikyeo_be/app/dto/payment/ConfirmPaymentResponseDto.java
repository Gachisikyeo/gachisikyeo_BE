package com.example.gachisikyeo_be.app.dto.payment;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchaseStatus;
import com.example.gachisikyeo_be.app.domain.payment.Payment;
import com.example.gachisikyeo_be.app.domain.payment.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ConfirmPaymentResponseDto {

    private Long paymentId;
    private Long participationId;
    private Long groupPurchaseId;

    private int amount;
    private PaymentStatus paymentStatus;

    private int currentQuantity;
    private int targetQuantity;
    private GroupPurchaseStatus groupPurchaseStatus;

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
