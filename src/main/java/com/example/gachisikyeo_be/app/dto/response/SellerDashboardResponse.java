package com.example.gachisikyeo_be.app.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "판매자 대시보드/총판매수량 응답 DTO")
@Getter
@Builder
public class SellerDashboardResponse {

    @Schema(description = "상품 총판매수량")
    private final long totalSoldQuantity;

    public static SellerDashboardResponse from(long totalSoldQuantity) {
        return SellerDashboardResponse.builder()
                .totalSoldQuantity(totalSoldQuantity)
                .build();
    }
}
