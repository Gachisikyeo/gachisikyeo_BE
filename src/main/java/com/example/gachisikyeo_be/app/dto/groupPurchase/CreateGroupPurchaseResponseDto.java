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

    @Schema(description = "총대 ID")
    private Long hostUserId;

    @Schema(description = "총대 닉네임")
    private String userNickName;

    @Schema(description = "총대 전화번호")
    private String hostContact;

    @Schema(description = "동 이름")
    private String dong;

    @Schema(description = "현재 주문(참여) 수량")
    private int currentQuantity;

    @Schema(description = "목표수량")
    private int targetQuantity;

    @Schema(description = "공구마감시간")
    private LocalDateTime groupEndAt;

    public static CreateGroupPurchaseResponseDto from(GroupPurchase gp) {
        return CreateGroupPurchaseResponseDto.builder()
                .groupPurchaseId(gp.getId())
                .hostContact(gp.getHostContact())
                .hostUserId(gp.getHostUser().getId())
                .userNickName(gp.getHostUser().getNickName())
                .currentQuantity(gp.getCurrentQuantity())
                .targetQuantity(gp.getTargetQuantity())
                .groupEndAt(gp.getGroupEndAt())
                .dong(gp.getHostUser().getLawDong().getDong())
                .build();
    }
}
