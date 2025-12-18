package com.example.gachisikyeo_be.app.dto;

import com.example.gachisikyeo_be.app.domain.region.LawDong;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "법정동 정보 응답 DTO")
@Getter
@Builder
public class LawDongDto {
    @Schema(description = "법정동 ID(PK)", example = "1")
    private Long id;         // LawDong PK

    @Schema(description = "법정동 코드", example = "1234567890")
    private String lawCode;  // 법정동 코드

    @Schema(description = "시/도", example = "서울특별시")
    private String sido;     // 시/도

    @Schema(description = "시/군/구", example = "구로구")
    private String sigungu;  // 시/군/구

    @Schema(description = "읍/면/동", example = "항동")
    private String dong;     // 읍/면/동

    public static LawDongDto from(LawDong entity) {
        if (entity == null) {
            return null;
        }
        return LawDongDto.builder()
                .id(entity.getId())
                .lawCode(entity.getLawCode())
                .sido(entity.getSido())
                .sigungu(entity.getSigungu())
                .dong(entity.getDong())
                .build();
    }
}
