package com.example.gachisikyeo_be.app.dto.response;

import com.example.gachisikyeo_be.app.domain.productRegistration.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "상품 등록 응답 DTO")
@Getter
@Builder
public class ProductRegistrationResponse {

    @Schema(description = "상품 ID")
    private Long id;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "총가격")
    private long price;

    @Schema(description = "재고수량")
    private int stockQuantity;

    @Schema(description = "상품이미지 url")
    private String imageUrl;

    @Schema(description = "구성수량")
    private int unitQuantity;

    @Schema(description = "개당가격")
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
