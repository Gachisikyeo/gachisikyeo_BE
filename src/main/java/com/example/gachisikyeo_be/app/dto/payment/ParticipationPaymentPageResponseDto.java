package com.example.gachisikyeo_be.app.dto.payment;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchase;
import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchaseStatus;
import com.example.gachisikyeo_be.app.domain.participation.Participation;
import com.example.gachisikyeo_be.app.domain.participation.ParticipationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ParticipationPaymentPageResponseDto {

    // participation
    private Long participationId;
    private ParticipationStatus participationStatus;
    private int quantity;
    private String buyerContact;
    private int shareAmount;
    private LocalDateTime createdAt;

    // groupPurchase
    private Long groupPurchaseId;
    private GroupPurchaseStatus groupPurchaseStatus;
    private LocalDateTime groupEndAt;
    private int currentQuantity;
    private int targetQuantity;
    private int minimumOrderUnit;

    // product (결제 화면 표시용)
    private Long productId;
    private String productName;
    private long unitPrice;

    public static ParticipationPaymentPageResponseDto from(Participation p) {
        GroupPurchase gp = p.getGroupPurchase();

        return ParticipationPaymentPageResponseDto.builder()
                .participationId(p.getId())
                .participationStatus(p.getStatus())
                .quantity(p.getQuantity())
                .buyerContact(p.getBuyerContact())
                .shareAmount(p.getShareAmount())
                .createdAt(p.getCreatedAt())

                .groupPurchaseId(gp.getId())
                .groupPurchaseStatus(gp.getStatus())
                .groupEndAt(gp.getGroupEndAt())
                .currentQuantity(gp.getCurrentQuantity())
                .targetQuantity(gp.getTargetQuantity())
                .minimumOrderUnit(gp.getMinimumOrderUnit())

                .productId(gp.getProduct().getId())
                .productName(gp.getProduct().getProductName())
                .unitPrice(gp.getProduct().getPrice())
                .build();
    }
}
