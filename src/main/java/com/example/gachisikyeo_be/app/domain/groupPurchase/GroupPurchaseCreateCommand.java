package com.example.gachisikyeo_be.app.domain.groupPurchase;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GroupPurchaseCreateCommand {
    private Long productId;
    private String title;
    private int targetQuantity;
    private int minimumOrderUnit;
    private LocalDateTime groupEndAt;
    private String pickupLocation;
    private LocalDateTime pickupAt;
}
