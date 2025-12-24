package com.example.gachisikyeo_be.app.domain.groupPurchase;

import com.example.gachisikyeo_be.app.domain.productRegistration.ProductRegistration;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GroupPurchaseCreateCommand {
    private ProductRegistration productRegistration;
    private int hostBuyQuantity;
    private int targetQuantity;
    private int minimumOrderUnit;
    private LocalDateTime groupEndAt;
    private String pickupLocation;
    private LocalDateTime pickupAt;
}
