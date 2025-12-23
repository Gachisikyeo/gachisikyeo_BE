package com.example.gachisikyeo_be.app.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SellerDashboardResponse {

    private final long totalSoldQuantity;

    public static SellerDashboardResponse from(long totalSoldQuantity) {
        return SellerDashboardResponse.builder()
                .totalSoldQuantity(totalSoldQuantity)
                .build();
    }
}
