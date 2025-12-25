package com.example.gachisikyeo_be.app.dto.mypage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "공구 조회 응답 DTO")
@Getter
@AllArgsConstructor
public class MypageGroupPurchaseDto {

    @Schema(description = "공구 ID")
    private Long groupPurchaseId;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "상품이미지 url")
    private String imageUrl;

    @Schema(description = "상품총가격")
    private int totalPrice;

    @Schema(description = "개당가격")
    private int unitPrice;

    @Schema(description = "상품총수량")
    private int quantity;

    @Schema(description = "공구 수령장소")
    private String pickupLocation;

    @Schema(description = "공구 수령시간")
    private LocalDateTime pickupTime;
}
