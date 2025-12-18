package com.example.gachisikyeo_be.app.dto.groupPurchase;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateGroupPurchaseRequestDto {

    @NotNull(message = "regionId는 필수입니다.")
    private Long regionId;

    @Min(value = 1, message = "hostBuyQuantity는 1 이상이어야 합니다.")
    private int hostBuyQuantity;

    @Min(value = 1, message = "targetQuantity는 1 이상이어야 합니다.")
    private int targetQuantity;

    @Min(value = 1, message = "minimumOrderUnit은 1 이상이어야 합니다.")
    private int minimumOrderUnit;

    @NotNull(message = "groupEndAt은 필수입니다.")
    @Future(message = "groupEndAt은 현재 시각 이후여야 합니다.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime groupEndAt;

    @NotBlank(message = "pickupLocation은 필수입니다.")
    @Size(max = 255, message = "pickupLocation은 최대 255자입니다.")
    private String pickupLocation;

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
