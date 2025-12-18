package com.example.gachisikyeo_be.app.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "사업자 정보 등록 요청 DTO")
@Getter
@NoArgsConstructor
public class BusinessInfoRequest {

    @Schema(description = "사업자 등록번호", example = "123-45-67890")
    @NotBlank
    private String businessNumber;   // 사업자 등록번호

    @Schema(description = "상호명", example = "꿀단지 농장")
    @NotBlank
    private String storeName;         // 상호명(법인명)

    @Schema(description = "대표자명", example = "왕꿀벌")
    @NotBlank
    private String ceoName;           // 대표자명

    @Schema(description = "사업장 주소", example = "서울특별시 강남구 테헤란로 123, 2층(역삼동, B빌딩)")
    @NotBlank
    private String address;           // 사업장 주소

    // 약관 동의
    @Schema(description = "판매자 이용약관 동의 여부", example = "true")
    @AssertTrue
    private boolean sellerTermsAgreed;

    @Schema(description = "개인정보 수집 및 이용 동의 여부", example = "true")
    @AssertTrue
    private boolean privacyPolicyAgreed;

    @Schema(description = "전자금융거래 이용약관 동의 여부", example = "true")
    @AssertTrue
    private boolean electronicFinanceAgreed;
}


