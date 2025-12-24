package com.example.gachisikyeo_be.app.dto.groupPurchase;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchase;
import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchaseStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GroupPurchaseListItemResponseDto {
    private Long groupPurchaseId;

    private String regionId;
    private String regionName;

    private Long hostUserId;
    private String userNickName;

    private int currentQuantity;
    private int targetQuantity;

    private LocalDateTime groupEndAt;
    private GroupPurchaseStatus status;

    public static GroupPurchaseListItemResponseDto from(GroupPurchase gp) {
        return GroupPurchaseListItemResponseDto.builder()
                .groupPurchaseId(gp.getId())
                .regionId(gp.getRegion().getLawCode())
                .regionName(gp.getRegion().getSido() + " " + gp.getRegion().getSigungu() + " " + gp.getRegion().getDong())
                .hostUserId(gp.getHostUser().getId())
                .userNickName(gp.getHostUser().getNickName())
                .currentQuantity(gp.getCurrentQuantity())
                .targetQuantity(gp.getTargetQuantity())
                .groupEndAt(gp.getGroupEndAt())
                .status(gp.getStatus())
                .build();
    }
}
