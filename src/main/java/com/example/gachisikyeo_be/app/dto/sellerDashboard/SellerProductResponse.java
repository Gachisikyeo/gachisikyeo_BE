package com.example.gachisikyeo_be.app.dto.sellerDashboard;

import com.example.gachisikyeo_be.app.domain.productRegistration.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class SellerProductResponse {

    private Long productId;
    private String productName;
    private long price;
    private int stock;
    private LocalDate createdAt;

    public static SellerProductResponse from(Product product) {
        return new SellerProductResponse(
                product.getId(),
                product.getProductName(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCreatedAt().toLocalDate()
        );
    }
}
