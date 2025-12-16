package com.example.gachisikyeo_be.app.dto.groupPurchase;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateGroupPurchaseRequestDto {
    private Long productId;
    private Long regionId;

    private int hostBuyQuantity;
    private int targetQuantity;
    private int minimumOrderUnit;

    private LocalDateTime groupEndAt;

    private String pickupLocation;
    private LocalDateTime pickupAt;
}
