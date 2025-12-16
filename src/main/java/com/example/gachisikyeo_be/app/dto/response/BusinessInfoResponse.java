package com.example.gachisikyeo_be.app.dto.response;

import com.example.gachisikyeo_be.app.domain.businessInfo.BusinessInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BusinessInfoResponse {

    private Long id;
    private Long userId;
    private String businessNumber;
    private String storeName;
    private String ceoName;
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
