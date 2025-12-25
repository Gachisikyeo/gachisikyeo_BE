package com.example.gachisikyeo_be.app.dto.businessInfo;

import com.example.gachisikyeo_be.app.domain.businessInfo.BusinessInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "사업자 정보 응답 DTO")
@Getter
@Builder
public class BusinessInfoResponse {

    @Schema(description = "사업자 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "사업자 번호", example = "123-45-67890")
    private String businessNumber;

    @Schema(description = "상호명", example = "꿀단지 농장")
    private String storeName;

    @Schema(description = "대표자명", example = "왕꿀벌")
    private String ceoName;

    @Schema(description = "사업장 주소", example = "서울특별시 강남구 테헤란로 123, 2층(역삼동, B빌딩)")
    private String address;

    public static BusinessInfoResponse from(BusinessInfo businessInfo) {
        return BusinessInfoResponse.builder()
                .id(businessInfo.getId())
                .userId(businessInfo.getUser().getId())
                .businessNumber(businessInfo.getBusinessNumber())
                .storeName(businessInfo.getStoreName())
                .ceoName(businessInfo.getCeoName())
                .address(businessInfo.getAddress())
                .build();
    }
}
