package com.example.gachisikyeo_be.app.dto.participation;

import com.example.gachisikyeo_be.app.domain.participation.Participation;
import com.example.gachisikyeo_be.app.domain.participation.ParticipationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CreateParticipationResponseDto {
    private Long participationId;
    private Long groupPurchaseId;
    private Long userId;
    private int quantity;
    private String buyerContact;
    private int shareAmount;
    private ParticipationStatus status;
    private LocalDateTime createdAt;

    public static CreateParticipationResponseDto from(Participation p) {
        return CreateParticipationResponseDto.builder()
                .participationId(p.getId())
                .groupPurchaseId(p.getGroupPurchase().getId())
                .userId(p.getUser().getId())
                .quantity(p.getQuantity())
                .buyerContact(p.getBuyerContact())
                .shareAmount(p.getShareAmount())
                .status(p.getStatus())
                .createdAt(p.getCreatedAt())
                .build();
    }
}
