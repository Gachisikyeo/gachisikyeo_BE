package com.example.gachisikyeo_be.app.controller.productRegistration;

import com.example.gachisikyeo_be.app.domain.productRegistration.ProductRegistration;
import com.example.gachisikyeo_be.app.dto.request.ProductRegistrationRequest;
import com.example.gachisikyeo_be.app.dto.response.ProductRegistrationResponse;
import com.example.gachisikyeo_be.global.code.SuccessCode;
import com.example.gachisikyeo_be.global.responseTemplate.ApiResponseTemplate;
import com.example.gachisikyeo_be.app.service.productRegistration.ProductRegistrationService;
import com.example.gachisikyeo_be.global.responseTemplate.ApiResponseTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductRegistrationController {

    private final ProductRegistrationService productRegistrationService;

    /**
     * 상품 등록
     * - JWT 인증된 사용자만 접근 가능
     * - 사업자(BusinessInfo) 보유 사용자만 등록 가능
     */
    @PostMapping
    public ResponseEntity<ApiResponseTemplate<ProductRegistrationResponse>> createProduct(
            @AuthenticationPrincipal String userIdStr,
            @Valid @RequestBody ProductRegistrationRequest request
    ) {
        Long userId = Long.parseLong(userIdStr);

        ProductRegistration product =
                productRegistrationService.create(userId, request);

        return ApiResponseTemplate.success(
                SuccessCode.PRODUCT_CREATED,
                ProductRegistrationResponse.from(product)
        );
    }
}
