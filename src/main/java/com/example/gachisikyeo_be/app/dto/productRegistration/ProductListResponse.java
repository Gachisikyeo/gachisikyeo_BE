package com.example.gachisikyeo_be.app.dto.productRegistration;

import com.example.gachisikyeo_be.app.domain.productRegistration.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "상품목록 응답 DTO")
@Getter
@Builder
public class ProductListResponse {

    @Schema(description = "상품 ID")
    private Long productId;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "총가격")
    private long price;

    @Schema(description = "개당가격")
    private long unitPrice;

    @Schema(description = "상품이미지 url")
    private String imageUrl;

    @Schema(description = "상호명(법인명)")
    private String storeName;

    public static ProductListResponse from(Product product) {
        return ProductListResponse.builder()
                .productId(product.getId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .unitPrice(product.getPrice() / product.getUnitQuantity())
                .imageUrl(product.getImageUrl())
                .storeName(product.getBusinessInfo().getStoreName())
                .build();
    }
}
