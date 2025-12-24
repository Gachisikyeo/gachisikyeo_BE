package com.example.gachisikyeo_be.app.dto.response;

import com.example.gachisikyeo_be.app.domain.productRegistration.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductListResponse {

    private Long productId;
    private String productName;
    private long price;
    private String imageUrl;
    private String storeName;

    public static ProductListResponse from(Product product) {
        return ProductListResponse.builder()
                .productId(product.getId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .storeName(product.getBusinessInfo().getStoreName())
                .build();
    }
}
