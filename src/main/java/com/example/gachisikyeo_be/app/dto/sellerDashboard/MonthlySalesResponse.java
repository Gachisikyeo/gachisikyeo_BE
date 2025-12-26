package com.example.gachisikyeo_be.app.dto.sellerDashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.YearMonth;

@Getter
@AllArgsConstructor
public class MonthlySalesResponse {

    private String yearMonth;        // 예: "2025-01"
    private long totalSalesAmount;

    public static MonthlySalesResponse of(int year, int month, long amount) {
        return new MonthlySalesResponse(
                YearMonth.of(year, month).toString(),
                amount
        );
    }
}
