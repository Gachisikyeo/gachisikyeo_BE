package com.example.gachisikyeo_be.app.dto.groupPurchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "공구 생성 요청 DTO")
@Getter
public class CreateGroupPurchaseRequestDto {

    @Schema(description = "공구 진행 지역의 ID")
    @NotNull(message = "regionId는 필수입니다.")
    private String regionId;

    @Schema(description = "총대가 구매하는 수량")
    @Min(value = 1, message = "hostBuyQuantity는 1 이상이어야 합니다.")
    private int hostBuyQuantity;

    @Schema(description = "목표수량")
    @Min(value = 1, message = "targetQuantity는 1 이상이어야 합니다.")
    private int targetQuantity;

    @Schema(description = "최소 주문가능 개수")
    @Min(value = 1, message = "minimumOrderUnit은 1 이상이어야 합니다.")
    private int minimumOrderUnit;

    @Schema(description = "공구마감시간")
    @NotNull(message = "groupEndAt은 필수입니다.")
    @Future(message = "groupEndAt은 현재 시각 이후여야 합니다.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime groupEndAt;

    @Schema(description = "공구 수령 장소")
    @NotBlank(message = "pickupLocation은 필수입니다.")
    @Size(max = 255, message = "pickupLocation은 최대 255자입니다.")
    private String pickupLocation;

    @Schema(description = "공구 수령 시간")
    @NotNull(message = "pickupAt은 필수입니다.")
    @Future(message = "pickupAt은 현재 시각 이후여야 합니다.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime pickupAt;

    @AssertTrue(message = "pickupAt은 groupEndAt 이후여야 합니다.")
    public boolean isPickupAfterEnd() {
        if (groupEndAt == null || pickupAt == null) return true; // @NotNull이 잡아줌
        return pickupAt.isAfter(groupEndAt);
    }
}
