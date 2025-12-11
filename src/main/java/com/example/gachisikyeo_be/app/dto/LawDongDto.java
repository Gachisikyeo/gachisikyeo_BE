package com.example.gachisikyeo_be.app.dto;

import com.example.gachisikyeo_be.app.domain.region.LawDong;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LawDongDto {
    private Long id;         // LawDong PK
    private String lawCode;  // 법정동 코드
    private String sido;     // 시/도
    private String sigungu;  // 시/군/구
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
