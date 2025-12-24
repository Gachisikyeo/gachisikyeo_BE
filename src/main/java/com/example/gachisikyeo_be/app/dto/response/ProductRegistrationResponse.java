package com.example.gachisikyeo_be.app.dto.response;

import com.example.gachisikyeo_be.app.domain.productRegistration.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductRegistrationResponse {

    private Long id;
    private String productName;
    private long price;
    private int stockQuantity;
    private String imageUrl;
    private int unitQuantity;
    private long unitPrice;

    public static ProductRegistrationResponse from(Product product) {
        return ProductRegistrationResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .unitQuantity(product.getUnitQuantity())
                .imageUrl(product.getImageUrl())
                .unitPrice(product.getPrice() / product.getUnitQuantity())
                .build();
    }
}
