package com.example.gachisikyeo_be.app.controller.sellerDashboard;

import com.example.gachisikyeo_be.app.dto.common.PageResponse;
import com.example.gachisikyeo_be.app.dto.sellerDashboard.SellerDashboardResponse;
import com.example.gachisikyeo_be.app.dto.sellerDashboard.SellerProductResponse;
import com.example.gachisikyeo_be.app.dto.sellerDashboard.SellerProductSortKey;
import com.example.gachisikyeo_be.app.service.sellerDashboard.SellerDashboardCountService;
import com.example.gachisikyeo_be.app.service.sellerDashboard.SellerDashboardProductService;
import com.example.gachisikyeo_be.global.code.SuccessCode;
import com.example.gachisikyeo_be.global.responseTemplate.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="SellerDashboard", description = "판매자 대시보드 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller/dashboard")
@Validated
public class SellerDashboardController {

    private final SellerDashboardCountService sellerDashboardService;
    private final SellerDashboardProductService sellerDashboardProductService;

    private static final int MAX_SIZE = 50;

    @Operation(
            summary = "상품 총 판매수량 조회",
            description = """
                판매자 대시보드 api,
                공동구매 성공 시 판매 완료된 상품들의 총수량 조회,
                판매자(SELLER)만 조회 가능
            """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/sales")
    public ResponseEntity<ApiResponseTemplate<SellerDashboardResponse>> getSellerSalesInfo(
            @AuthenticationPrincipal String userIdStr
    ) {
        Long userId = Long.parseLong(userIdStr);

        long totalSoldQuantity =
                sellerDashboardService.getTotalSoldQuantity(userId);

        return ApiResponseTemplate.success(
                SuccessCode.SELLER_DASHBOARD_SUCCESS,
                SellerDashboardResponse.from(totalSoldQuantity)
        );
    }

    @Operation(
            summary = "판매자 상품관리 목록 조회",
            description = "판매자가 등록한 상품 목록을 페이지네이션하여 조회",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/products")
    public ResponseEntity<ApiResponseTemplate<PageResponse<SellerProductResponse>>> getMyProducts(
            @AuthenticationPrincipal String userIdStr,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "8") @Min(1) @Max(MAX_SIZE) int size,
            @RequestParam(defaultValue = "CREATED_AT") SellerProductSortKey sortKey,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction
    ) {
        Long sellerId = Long.parseLong(userIdStr);

        PageRequest pageable = PageRequest.of(page, size, Sort.by(direction, sortKey.getProperty()));

        Page<SellerProductResponse> result =
                sellerDashboardProductService.getMyProducts(sellerId, pageable);

        return ApiResponseTemplate.success(
                SuccessCode.SELLER_PRODUCT_LIST_FETCHED,
                PageResponse.from(result)
        );
    }
}
