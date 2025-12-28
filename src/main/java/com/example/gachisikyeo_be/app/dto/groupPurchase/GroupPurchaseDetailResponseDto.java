package com.example.gachisikyeo_be.app.dto.groupPurchase;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "공구 조회 응답 DTO")
@Getter
@Builder
public class GroupPurchaseDetailResponseDto {
    @Schema(description = "상품 이름")
    private String productName;

    @Schema(description = "공구 목표 수량")
    private int targetQuantity;

    @Schema(description = "공구 마감 기한")
    private LocalDateTime groupEndAt;

    @Schema(description = "공구 수령 장소")
    private String pickupLocation;

    @Schema(description = "공구 수령 시간")
    private LocalDateTime pickupAt;

    @Schema(description = "최소 주문 수량")
    private int minimumOrderUnit;

    public static GroupPurchaseDetailResponseDto from(GroupPurchase gp) {
        return GroupPurchaseDetailResponseDto.builder()
                .productName(gp.getProduct().getProductName())
                .targetQuantity(gp.getTargetQuantity())
                .groupEndAt(gp.getGroupEndAt())
                .pickupLocation(gp.getPickupLocation())
                .pickupAt(gp.getPickupAt())
                .minimumOrderUnit(gp.getMinimumOrderUnit())
                .build();
    }
}

