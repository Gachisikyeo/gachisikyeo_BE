package com.example.gachisikyeo_be.app.dto.productRegistration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductSortKey {
    CREATED_AT("createdAt"),
    VIEW_COUNT("viewCount"),
    PRICE("price");

    private final String property;
}
