package com.example.gachisikyeo_be.app.dto.groupPurchase;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "공구 생성 응답 DTO")
@Getter
@Builder
public class CreateGroupPurchaseResponseDto {
    @Schema(description = "공구 ID")
    private Long groupPurchaseId;


    @Schema(description = "공구 진행 지역의 ID")
    private Long regionId;

    @Schema(description = "공구 진행 지역의 주소")
    private String regionName;


    @Schema(description = "총대 ID")
    private Long hostUserId;

    @Schema(description = "총대 닉네임")
    private String userNickName;


    @Schema(description = "현재 주문(참여) 수량")
    private int currentQuantity;

    @Schema(description = "목표수량")
    private int targetQuantity;

    // here : 여기에 총 몇 명이 해당 공구에 참가중인지 넣어야 함

    @Schema(description = "공구마감시간")
    private LocalDateTime groupEndAt;

    public static CreateGroupPurchaseResponseDto from(GroupPurchase gp) {
        return CreateGroupPurchaseResponseDto.builder()
                .groupPurchaseId(gp.getId())
                .regionId(gp.getRegion().getId())
                .regionName(gp.getRegion().getSido() + " " + gp.getRegion().getSigungu() + " " + gp.getRegion().getDong())
                .hostUserId(gp.getHostUser().getId())
                .userNickName(gp.getHostUser().getNickName())
                .currentQuantity(gp.getCurrentQuantity())
                .targetQuantity(gp.getTargetQuantity())
                .groupEndAt(gp.getGroupEndAt())
                .build();
    }
}
