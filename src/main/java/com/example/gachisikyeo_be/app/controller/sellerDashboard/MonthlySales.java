package com.example.gachisikyeo_be.app.controller.sellerDashboard;

import com.example.gachisikyeo_be.app.dto.sellerDashboard.MonthlySalesResponse;
import com.example.gachisikyeo_be.app.service.sellerDashboard.SellerDashboardSalesService;
import com.example.gachisikyeo_be.global.code.ErrorCode;
import com.example.gachisikyeo_be.global.code.SuccessCode;
import com.example.gachisikyeo_be.global.exception.BusinessException;
import com.example.gachisikyeo_be.global.responseTemplate.ApiResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller/dashboard")
public class MonthlySales {

    private final SellerDashboardSalesService sellerDashboardSalesService;

    /**
     * 판매자 월별 매출 조회
     * 예) /api/seller/dashboard/monthly-sales?year=2025&month=1
     */
    @GetMapping("/monthly-sales")
    public ResponseEntity<ApiResponseTemplate<MonthlySalesResponse>> getMonthlySales(
            @RequestParam int year,
            @RequestParam int month,
            Authentication authentication
    ) {
        Long sellerId = extractUserId(authentication);

        long totalAmount =
                sellerDashboardSalesService.getMonthlySales(sellerId, year, month);

        return ApiResponseTemplate.success(
                SuccessCode.SELLER_MONTHLY_SALES_FETCHED,
                MonthlySalesResponse.of(year, month, totalAmount)
        );
    }

    private Long extractUserId(Authentication authentication) {
        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new BusinessException(ErrorCode.AUTH_REQUIRED);
        }
        return Long.valueOf(authentication.getName());
    }
}
