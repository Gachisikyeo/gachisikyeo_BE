package com.example.gachisikyeo_be.app.dto.payment;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchase;
import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchaseStatus;
import com.example.gachisikyeo_be.app.domain.participation.Participation;
import com.example.gachisikyeo_be.app.domain.participation.ParticipationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(description = "공구 참여자 결제페이지 응답 DTO")
@Getter
@Builder
public class ParticipationPaymentPageResponseDto {

    // participation
    @Schema(description = "공구 참여자 ID")
    private Long participationId;

    @Schema(description = "참여자 status(PENDING, FAILED 등)")
    private ParticipationStatus participationStatus;

    @Schema(description = "참여자가 구매할 수량")
    private int quantity;

    @Schema(description = "참여자(본인) 연락처")
    private String buyerContact;

    @Schema(description = "참여자 총 결제금액")
    private int shareAmount;

    @Schema(description = "참여자 생성 시간")
    private LocalDateTime createdAt;

    // groupPurchase
    @Schema(description = "공구 ID")
    private Long groupPurchaseId;

    @Schema(description = "공구 status(OPEN, SUCCESS 등)")
    private GroupPurchaseStatus groupPurchaseStatus;

    @Schema(description = "공구마감시간")
    private LocalDateTime groupEndAt;

    @Schema(description = "현재 주문(참여) 수량")
    private int currentQuantity;

    @Schema(description = "목표수량")
    private int targetQuantity;

    @Schema(description = "최소주문가능개수")
    private int minimumOrderUnit;

    // product (결제 화면 표시용)
    @Schema(description = "상품 ID")
    private Long productId;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "개당가격")
    private long unitPrice;

    public static ParticipationPaymentPageResponseDto from(Participation p) {
        GroupPurchase gp = p.getGroupPurchase();

        return ParticipationPaymentPageResponseDto.builder()
                .participationId(p.getId())
                .participationStatus(p.getStatus())
                .quantity(p.getQuantity())
                .buyerContact(p.getBuyerContact())
                .shareAmount(p.getShareAmount())
                .createdAt(p.getCreatedAt())

                .groupPurchaseId(gp.getId())
                .groupPurchaseStatus(gp.getStatus())
                .groupEndAt(gp.getGroupEndAt())
                .currentQuantity(gp.getCurrentQuantity())
                .targetQuantity(gp.getTargetQuantity())
                .minimumOrderUnit(gp.getMinimumOrderUnit())

                .productId(gp.getProduct().getId())
                .productName(gp.getProduct().getProductName())
                .unitPrice(gp.getProduct().getPrice())
                .build();
    }
}
