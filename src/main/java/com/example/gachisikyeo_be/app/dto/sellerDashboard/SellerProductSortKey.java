package com.example.gachisikyeo_be.app.dto.sellerDashboard;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SellerProductSortKey {
    CREATED_AT("createdAt"),
    PRICE("price"),
    STOCK_QUANTITY("stockQuantity");

    private final String property;
}
