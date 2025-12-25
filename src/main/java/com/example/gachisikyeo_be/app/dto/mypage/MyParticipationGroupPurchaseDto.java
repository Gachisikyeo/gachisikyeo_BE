package com.example.gachisikyeo_be.app.dto.mypage;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchaseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyParticipationGroupPurchaseDto {

    private final Long participationId;
    private final Long groupPurchaseId;

    private final String productName;
    private final String imageUrl;

    private final int totalPrice;     // 팀 코드 기준 유지
    private final int unitPrice;      // 팀 코드 기준 유지
    private final int totalQuantity;  // 공구 목표수량(팀 코드 기준 유지)

    private final int myQuantity;      // 내가 참여한 수량
    private final int myPaymentAmount; // 내가 낼 금액(임시)

    private final String pickupLocation;
    private final LocalDateTime pickupTime;

    private final GroupPurchaseStatus groupPurchaseStatus;
}
