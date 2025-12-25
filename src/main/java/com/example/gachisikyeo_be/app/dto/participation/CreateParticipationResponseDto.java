package com.example.gachisikyeo_be.app.dto.participation;

import com.example.gachisikyeo_be.app.domain.participation.Participation;
import com.example.gachisikyeo_be.app.domain.participation.ParticipationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "공구 참여자 생성 응답 DTO")
@Getter
@Builder
public class CreateParticipationResponseDto {
    @Schema(description = "참여자 ID")
    private Long participationId;

    @Schema(description = "공구 ID")
    private Long groupPurchaseId;

    @Schema(description = "사용자 ID")
    private Long userId;

    @Schema(description = "참여자가 구매할 수량")
    private int quantity;

    @Schema(description = "참여자(본인) 연락처")
    private String buyerContact;

    @Schema(description = "참여자 총 결제금액")
    private int shareAmount;

    @Schema(description = "참여자 status(PENDING, FAILED 등)")
    private ParticipationStatus status;

    @Schema(description = "참여자 생성 시간")
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
