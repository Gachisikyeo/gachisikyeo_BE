package com.example.gachisikyeo_be.app.dto.groupPurchase;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchase;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CreateGroupPurchaseResponseDto {
    private Long groupPurchaseId;
    private Long regionId;
    private String userNickName;
    private int currentQuantity;
    private int targetQuantity;
    // here : 여기에 총 몇 명이 해당 공구에 참가중인지 넣어야 함
    private LocalDateTime groupEndAt;
}
