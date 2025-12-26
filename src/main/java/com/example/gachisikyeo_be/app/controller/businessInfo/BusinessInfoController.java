package com.example.gachisikyeo_be.app.controller.businessInfo;

import com.example.gachisikyeo_be.app.domain.businessInfo.BusinessInfo;
import com.example.gachisikyeo_be.app.dto.businessInfo.BusinessInfoRequest;
import com.example.gachisikyeo_be.app.dto.businessInfo.BusinessInfoResponse;
import com.example.gachisikyeo_be.app.service.businessInfo.BusinessInfoService;
import com.example.gachisikyeo_be.global.code.SuccessCode;
import com.example.gachisikyeo_be.global.responseTemplate.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "BusinessInfo", description = "판매자(사업자) 정보 등록 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/business-info")
public class BusinessInfoController {

    private final BusinessInfoService businessInfoService;

    @Operation(
            summary = "판매자(사업자) 정보 등록",
            description = """
                    판매자(사업자) 정보를 등록합니다.
                    JWT 인증된 사용자만 등록 가능합니다.
                    """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "201", description = "상점 등록 성공")
    @PostMapping
    public ResponseEntity<ApiResponseTemplate<BusinessInfoResponse>> createBusinessInfo(
            @AuthenticationPrincipal String userIdStr,
            @Valid @RequestBody BusinessInfoRequest request
    ) {
        Long userId = Long.parseLong(userIdStr);

        BusinessInfo businessInfo =
                businessInfoService.create(userId, request);

        return ApiResponseTemplate.success(
                SuccessCode.BUSINESS_ENROLL_SUCCESS,
                BusinessInfoResponse.from(businessInfo)
        );
    }

    @Operation(
            summary = "내 판매자(사업자) 정보 조회",
            description = "판매자 등록 여부 확인 용도. 등록되어 있으면 200, 없으면 404",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/me")
    public ResponseEntity<ApiResponseTemplate<BusinessInfoResponse>> getMyBusinessInfo(
            @AuthenticationPrincipal String userIdStr
    ) {
        Long userId = Long.parseLong(userIdStr);

        BusinessInfo businessInfo = businessInfoService.getMyBusinessInfo(userId);

        return ApiResponseTemplate.success(
                SuccessCode.BUSINESS_INFO_FETCH_SUCCESS,
                BusinessInfoResponse.from(businessInfo)
        );
    }
}
