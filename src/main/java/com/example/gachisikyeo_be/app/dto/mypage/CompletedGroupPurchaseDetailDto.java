package com.example.gachisikyeo_be.app.dto.mypage;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "완료된 공구 상세 정보 조회 응답 DTO")
@Getter
@AllArgsConstructor
public class CompletedGroupPurchaseDetailDto {

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "상품이미지 url")
    private String imageUrl;

    @Schema(description = "총가격")
    private int totalPrice;

    @Schema(description = "개당가격")
    private int unitPrice;

    @Schema(description = "총수량")
    private int quantity;

    @Schema(description = "공구 수령장소")
    private String pickupLocation;

    @Schema(description = "공구 수령시간")
    private LocalDateTime pickupTime;


    @Schema(description = "주문번호(공구 ID)")
    private Long orderNumber;

    @Schema(description = "구매자명(참여자 닉네임)")
    private String buyerName;

    @Schema(description = "총대 닉네임")
    private String leaderNickname;

    @Schema(description = "참여자 결제금액")
    private int paymentAmount;
}
