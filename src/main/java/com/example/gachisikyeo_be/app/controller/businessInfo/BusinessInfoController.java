package com.example.gachisikyeo_be.app.controller.businessInfo;

import com.example.gachisikyeo_be.app.domain.businessInfo.BusinessInfo;
import com.example.gachisikyeo_be.app.dto.request.BusinessInfoRequest;
import com.example.gachisikyeo_be.app.dto.response.BusinessInfoResponse;
import com.example.gachisikyeo_be.app.service.businessInfo.BusinessInfoService;
import com.example.gachisikyeo_be.global.code.SuccessCode;
import com.example.gachisikyeo_be.global.responseTemplate.ApiResponseTemplate;
import com.example.gachisikyeo_be.global.users.domain.auth.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/business-info")
public class BusinessInfoController {

    private final BusinessInfoService businessInfoService;

    /**
     * 판매자 사업자 정보 등록
     * - JWT 인증된 사용자만 접근 가능
     * - SELLER 여부 검증은 Service에서 처리
     */
//    @PostMapping
//    public ResponseEntity<BusinessInfoResponse> createBusinessInfo(
//            @AuthenticationPrincipal User user,
//            @RequestBody BusinessInfoRequest request
//    ) {
//        BusinessInfo businessInfo =
//                businessInfoService.create(user.getId(), request);
//
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(BusinessInfoResponse.from(businessInfo));
//    }

    @PostMapping
    public ResponseEntity<ApiResponseTemplate<BusinessInfoResponse>> createBusinessInfo(
            @AuthenticationPrincipal String userIdStr,
            @Valid @RequestBody BusinessInfoRequest request
    ) {
        Long userId = Long.parseLong(userIdStr);
        BusinessInfo businessInfo =
                businessInfoService.create(userId, request);

        return ApiResponseTemplate.success(SuccessCode.BUSINESS_ENROLL_SUCCESS, BusinessInfoResponse.from(businessInfo));
    }
}
