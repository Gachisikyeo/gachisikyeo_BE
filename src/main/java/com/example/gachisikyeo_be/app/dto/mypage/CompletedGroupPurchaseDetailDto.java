package com.example.gachisikyeo_be.app.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompletedGroupPurchaseDetailDto {

    private String productName;
    private String imageUrl;
    private int totalPrice;
    private int unitPrice;
    private int quantity;

    private Long orderNumber;
    private String buyerName;
    private String leaderNickname;
    private int paymentAmount;
}
