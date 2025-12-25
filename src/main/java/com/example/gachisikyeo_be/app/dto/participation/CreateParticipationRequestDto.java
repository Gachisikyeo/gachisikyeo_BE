package com.example.gachisikyeo_be.app.dto.participation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "공구 참여자 생성 요청 DTO")
@Getter
@NoArgsConstructor
public class CreateParticipationRequestDto {
    @Schema(description = "참여자가 구매할 수량")
    @Min(value = 1, message = "quantity는 1 이상이어야 합니다.")
    private int quantity;

    @Schema(description = "참여자(본인) 연락처")
    @NotBlank(message = "buyerContact는 필수입니다.")
    @Size(max = 30, message = "buyerContact는 30자 이하여야 합니다.")
    private String buyerContact;
}
