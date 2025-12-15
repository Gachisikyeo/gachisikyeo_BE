package com.example.gachisikyeo_be.app.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BusinessInfoRequest {

    @NotBlank
    private String businessNumber;   // 사업자 등록번호

    @NotBlank
    private String storeName;         // 상호명(법인명)

    @NotBlank
    private String ceoName;           // 대표자명

    @NotBlank
    private String address;           // 사업장 주소

    // 약관 동의
    @AssertTrue
    private boolean sellerTermsAgreed;

    @AssertTrue
    private boolean privacyPolicyAgreed;

    @AssertTrue
    private boolean electronicFinanceAgreed;
}


