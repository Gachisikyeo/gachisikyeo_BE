package com.example.gachisikyeo_be.app.dto.participation;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateParticipationRequestDto {
    @Min(value = 1, message = "quantity는 1 이상이어야 합니다.")
    private int quantity;

    @NotBlank(message = "buyerContact는 필수입니다.")
    @Size(max = 30, message = "buyerContact는 30자 이하여야 합니다.")
    private String buyerContact;
}
