package com.example.gachisikyeo_be.app.controller.productRegistration;

import com.example.gachisikyeo_be.app.domain.productRegistration.Product;
import com.example.gachisikyeo_be.app.domain.productRegistration.ProductCategory;
import com.example.gachisikyeo_be.app.dto.productRegistration.ProductListResponse;
import com.example.gachisikyeo_be.app.dto.productRegistration.ProductRegistrationRequest;
import com.example.gachisikyeo_be.app.dto.productRegistration.ProductRegistrationResponse;
import com.example.gachisikyeo_be.app.service.productRegistration.ProductRegistrationService;
import com.example.gachisikyeo_be.global.code.SuccessCode;
import com.example.gachisikyeo_be.global.responseTemplate.ApiResponseTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "ProductRegistration", description = "상품 등록/조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductRegistrationController {

    private final ProductRegistrationService productRegistrationService;

    /* =========================
       상품 등록
    ========================= */
    @Operation(
            summary = "상품 등록",
            description = "상품 정보(data) + 이미지(multipart/form-data)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseTemplate<ProductRegistrationResponse>> createProduct(
            @AuthenticationPrincipal String userIdStr,
            @RequestPart("data") String data,
            @RequestPart("image") MultipartFile image
    ) throws Exception {

        Long userId = Long.parseLong(userIdStr);
        ProductRegistrationRequest request =
                new ObjectMapper().readValue(data, ProductRegistrationRequest.class);

        Product product = productRegistrationService.create(userId, request, image);

        return ApiResponseTemplate.success(
                SuccessCode.PRODUCT_CREATED,
                ProductRegistrationResponse.from(product)
        );
    }

    /* =========================
       판매자 상품 관리 (8개씩)
    ========================= */
    @Operation(
            summary = "내 상품 조회 (판매자)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/my")
    public ResponseEntity<ApiResponseTemplate<Page<ProductRegistrationResponse>>> getMyProducts(
            @AuthenticationPrincipal String userIdStr,
            @PageableDefault(size = 8, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Long userId = Long.parseLong(userIdStr);

        Page<ProductRegistrationResponse> page =
                productRegistrationService.getMyProducts(userId, pageable)
                        .map(ProductRegistrationResponse::from);

        return ApiResponseTemplate.success(
                SuccessCode.PRODUCT_LIST_SUCCESS,
                page
        );
    }

    /* =========================
       전체 상품 목록 (구매자, 12개씩)
    ========================= */
    @Operation(summary = "전체 상품 목록 조회 (구매자)")
    @GetMapping
    public ResponseEntity<ApiResponseTemplate<Page<ProductRegistrationResponse>>> getAllProducts(
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<ProductRegistrationResponse> page =
                productRegistrationService.getAllProducts(pageable)
                        .map(ProductRegistrationResponse::from);

        return ApiResponseTemplate.success(
                SuccessCode.PRODUCT_LIST_SUCCESS,
                page
        );
    }

    /* =========================
       상품 상세 조회
    ========================= */
    @Operation(summary = "상품 상세 조회")
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponseTemplate<ProductRegistrationResponse>> getProductDetail(
            @PathVariable Long productId
    ) {
        Product product = productRegistrationService.getProductDetail(productId);

        return ApiResponseTemplate.success(
                SuccessCode.PRODUCT_DETAIL_SUCCESS,
                ProductRegistrationResponse.from(product)
        );
    }

    /* =========================
       인기 상품 조회 (조회수 기준)
    ========================= */
    @Operation(summary = "인기 상품 조회")
    @GetMapping("/popular")
    public ResponseEntity<ApiResponseTemplate<Page<ProductListResponse>>> getPopularProducts(
            @PageableDefault(size = 12, sort = "viewCount", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<ProductListResponse> page =
                productRegistrationService.getAllProducts(pageable)
                        .map(ProductListResponse::from);

        return ApiResponseTemplate.success(
                SuccessCode.PRODUCT_LIST_SUCCESS,
                page
        );
    }

    /* =========================
       카테고리별 상품 조회
    ========================= */
    @Operation(summary = "카테고리별 상품 조회")
    @GetMapping("/category")
    public ResponseEntity<ApiResponseTemplate<Page<ProductRegistrationResponse>>> getProductsByCategory(
            @RequestParam ProductCategory category,
            @PageableDefault(size = 12, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<ProductRegistrationResponse> page =
                productRegistrationService.getProductsByCategory(category, pageable)
                        .map(ProductRegistrationResponse::from);

        return ApiResponseTemplate.success(
                SuccessCode.PRODUCT_LIST_SUCCESS,
                page
        );
    }
}
