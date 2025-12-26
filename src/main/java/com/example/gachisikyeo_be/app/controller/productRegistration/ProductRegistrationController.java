package com.example.gachisikyeo_be.app.controller.productRegistration;

import com.example.gachisikyeo_be.app.domain.productRegistration.Product;
import com.example.gachisikyeo_be.app.domain.productRegistration.ProductCategory;
import com.example.gachisikyeo_be.app.dto.common.PageResponse;
import com.example.gachisikyeo_be.app.dto.productRegistration.ProductListResponse;
import com.example.gachisikyeo_be.app.dto.productRegistration.ProductRegistrationRequest;
import com.example.gachisikyeo_be.app.dto.productRegistration.ProductRegistrationResponse;
import com.example.gachisikyeo_be.app.dto.productRegistration.ProductSortKey;
import com.example.gachisikyeo_be.app.service.productRegistration.ProductRegistrationService;
import com.example.gachisikyeo_be.global.code.SuccessCode;
import com.example.gachisikyeo_be.global.responseTemplate.ApiResponseTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "ProductRegistration", description = "상품 등록/조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Validated
public class ProductRegistrationController {

    private final ProductRegistrationService productRegistrationService;

    // size 상한: 프론트/트래픽 고려해 적당히 제한(필요 시 조정)
    private static final int MAX_SIZE = 50;

    /* =========================
       상품 등록
    ========================= */
//    @Operation(
//            summary = "상품 등록",
//            description = "상품 정보(data) + 이미지(multipart/form-data)",
//            security = @SecurityRequirement(name = "bearerAuth")
//    )
//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<ApiResponseTemplate<ProductRegistrationResponse>> createProduct(
//            Authentication authentication,
//            @RequestPart("data") String data,
//            @RequestPart("image") MultipartFile image
//    ) throws Exception {
//        Long userId = Long.parseLong(authentication.getName());
//        ProductRegistrationRequest request =
//                new ObjectMapper().readValue(data, ProductRegistrationRequest.class);
//
//        Product product = productRegistrationService.create(userId, request, image);
//
//        return ApiResponseTemplate.success(
//                SuccessCode.PRODUCT_CREATED,
//                ProductRegistrationResponse.from(product)
//        );
//    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseTemplate<ProductRegistrationResponse>> createProduct(
            Authentication authentication,
            @RequestPart("data") String data,
            @RequestPart("image") MultipartFile image
    ) throws Exception {

        Long userId = Long.parseLong(authentication.getName());
        ProductRegistrationRequest request = new ObjectMapper().readValue(data, ProductRegistrationRequest.class);

        try {
            Product product = productRegistrationService.create(userId, request, image);

            return ApiResponseTemplate.success(
                    SuccessCode.PRODUCT_CREATED,
                    ProductRegistrationResponse.from(product)
            );
        } catch (Exception e) {
            // ✅ 이게 핵심: 원인 예외를 stdout에 그대로 남김
            e.printStackTrace();
            throw e; // 전역 핸들러가 500 내려주는 건 유지
        }
    }

    /* =========================
       판매자 상품 관리
       GET /api/products/my?page=0&size=8&sortKey=CREATED_AT&direction=DESC
    ========================= */
    @Operation(summary = "내 상품 조회 (판매자)", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/my")
    public ResponseEntity<ApiResponseTemplate<PageResponse<ProductRegistrationResponse>>> getMyProducts(
            @AuthenticationPrincipal String userIdStr,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "8") @Min(1) @Max(MAX_SIZE) int size,
            @RequestParam(defaultValue = "CREATED_AT") ProductSortKey sortKey,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        Long userId = Long.parseLong(userIdStr);

        PageRequest pageable = PageRequest.of(page, size, Sort.by(direction, sortKey.getProperty()));

        Page<ProductRegistrationResponse> result =
                productRegistrationService.getMyProducts(userId, pageable)
                        .map(ProductRegistrationResponse::from);

        return ApiResponseTemplate.success(
                SuccessCode.PRODUCT_LIST_SUCCESS,
                PageResponse.from(result)
        );
    }

    /* =========================
       전체 상품 목록
       GET /api/products?page=0&size=12&sortKey=CREATED_AT&direction=DESC
    ========================= */
    @Operation(summary = "전체 상품 목록 조회 (구매자)")
    @GetMapping
    public ResponseEntity<ApiResponseTemplate<PageResponse<ProductRegistrationResponse>>> getAllProducts(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "12") @Min(1) @Max(MAX_SIZE) int size,
            @RequestParam(defaultValue = "CREATED_AT") ProductSortKey sortKey,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(direction, sortKey.getProperty()));

        Page<ProductRegistrationResponse> result =
                productRegistrationService.getAllProducts(pageable)
                        .map(ProductRegistrationResponse::from);

        return ApiResponseTemplate.success(
                SuccessCode.PRODUCT_LIST_SUCCESS,
                PageResponse.from(result)
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
       인기 상품 조회 (정렬 고정: VIEW_COUNT DESC)
       GET /api/products/popular?page=0&size=12
    ========================= */
    @Operation(summary = "인기 상품 조회")
    @GetMapping("/popular")
    public ResponseEntity<ApiResponseTemplate<PageResponse<ProductListResponse>>> getPopularProducts(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "12") @Min(1) @Max(MAX_SIZE) int size
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "viewCount"));

        Page<ProductListResponse> result =
                productRegistrationService.getAllProducts(pageable)
                        .map(ProductListResponse::from);

        return ApiResponseTemplate.success(
                SuccessCode.PRODUCT_LIST_SUCCESS,
                PageResponse.from(result)
        );
    }

    /* =========================
       카테고리별 상품 조회
       GET /api/products/category?category=FOOD&page=0&size=12&sortKey=CREATED_AT&direction=DESC
    ========================= */
    @Operation(summary = "카테고리별 상품 조회")
    @GetMapping("/category")
    public ResponseEntity<ApiResponseTemplate<PageResponse<ProductRegistrationResponse>>> getProductsByCategory(
            @RequestParam ProductCategory category,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "12") @Min(1) @Max(MAX_SIZE) int size,
            @RequestParam(defaultValue = "CREATED_AT") ProductSortKey sortKey,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(direction, sortKey.getProperty()));

        Page<ProductRegistrationResponse> result =
                productRegistrationService.getProductsByCategory(category, pageable)
                        .map(ProductRegistrationResponse::from);

        return ApiResponseTemplate.success(
                SuccessCode.PRODUCT_LIST_SUCCESS,
                PageResponse.from(result)
        );
    }
}
