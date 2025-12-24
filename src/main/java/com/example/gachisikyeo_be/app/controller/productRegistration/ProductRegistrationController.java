package com.example.gachisikyeo_be.app.controller.productRegistration;

import com.example.gachisikyeo_be.app.domain.productRegistration.Product;
import com.example.gachisikyeo_be.app.domain.productRegistration.ProductCategory;
import com.example.gachisikyeo_be.app.dto.request.ProductRegistrationRequest;
import com.example.gachisikyeo_be.app.dto.response.ProductListResponse;
import com.example.gachisikyeo_be.app.dto.response.ProductRegistrationResponse;
import com.example.gachisikyeo_be.app.service.productRegistration.ProductRegistrationService;
import com.example.gachisikyeo_be.global.code.SuccessCode;
import com.example.gachisikyeo_be.global.responseTemplate.ApiResponseTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductRegistrationController {

    private final ProductRegistrationService productRegistrationService;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ApiResponseTemplate<ProductRegistrationResponse>> createProduct(
            @AuthenticationPrincipal String userIdStr,

            @RequestPart("data") String data,
            @RequestPart("image") MultipartFile image
    ) throws Exception {

        Long userId = Long.parseLong(userIdStr);

        ObjectMapper objectMapper = new ObjectMapper();
        ProductRegistrationRequest request =
                objectMapper.readValue(data, ProductRegistrationRequest.class);

        Product product =
                productRegistrationService.create(userId, request, image);

        return ApiResponseTemplate.success(
                SuccessCode.PRODUCT_CREATED,
                ProductRegistrationResponse.from(product)
        );
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponseTemplate<List<ProductRegistrationResponse>>> getMyProducts(
            @AuthenticationPrincipal String userIdStr
    ) {
        Long userId = Long.parseLong(userIdStr);

        List<Product> products =
                productRegistrationService.getMyProducts(userId);

        List<ProductRegistrationResponse> responses =
                products.stream()
                        .map(ProductRegistrationResponse::from)
                        .toList();

        return ApiResponseTemplate.success(
                SuccessCode.PRODUCT_LIST_SUCCESS,
                responses
        );
    }

    /**
     * 전체 상품 목록 (구매자 / 메인 화면)
     */
    @GetMapping
    public ResponseEntity<ApiResponseTemplate<List<ProductRegistrationResponse>>> getAllProducts() {

        List<Product> products =
                productRegistrationService.getAllProducts();

        List<ProductRegistrationResponse> responses =
                products.stream()
                        .map(ProductRegistrationResponse::from)
                        .toList();

        return ApiResponseTemplate.success(
                SuccessCode.PRODUCT_LIST_SUCCESS,
                responses
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponseTemplate<ProductRegistrationResponse>> getProductDetail(
            @PathVariable Long productId
    ) {
        Product product =
                productRegistrationService.getProductDetail(productId);

        return ApiResponseTemplate.success(
                SuccessCode.PRODUCT_DETAIL_SUCCESS,
                ProductRegistrationResponse.from(product)
        );
    }

    @GetMapping("/popular")
    public ResponseEntity<ApiResponseTemplate<List<ProductListResponse>>> getPopularProducts() {

        List<Product> products =
                productRegistrationService.getProductsOrderByViewCount();

        List<ProductListResponse> responses =
                products.stream()
                        .map(ProductListResponse::from)
                        .toList();

        return ApiResponseTemplate.success(
                SuccessCode.PRODUCT_LIST_SUCCESS,
                responses
        );
    }

    @GetMapping("/category")
    public ResponseEntity<ApiResponseTemplate<List<ProductRegistrationResponse>>> getProductsByCategory(
            @RequestParam ProductCategory category
    ) {
        List<Product> products =
                productRegistrationService.getProductsByCategoryOrderByCreatedAt(category);

        List<ProductRegistrationResponse> responses =
                products.stream()
                        .map(ProductRegistrationResponse::from)
                        .toList();

        return ApiResponseTemplate.success(
                SuccessCode.PRODUCT_LIST_SUCCESS,
                responses
        );
    }

}
