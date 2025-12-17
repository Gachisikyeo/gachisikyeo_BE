package com.example.gachisikyeo_be.app.domain.productRegistration;

import com.example.gachisikyeo_be.app.domain.businessInfo.BusinessInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ProductRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이 상품을 등록한 판매자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_info_id", nullable = false)
    private BusinessInfo businessInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category; //카테고리

    @Column(nullable = false)
    private String productName; //상품이름

    @Column(nullable = false)
    private long price; //가격

    @Column(nullable = false)
    private int stockQuantity; // 재고수량

    @Column(nullable = false)
    private int unitQuantity; // 구성수량


    @Column(nullable = false)
    private String imageUrl; // 업로드 이미지 url

    @Column(nullable = false)
    private String descriptionTitle; // 설명 제목(요약)

    @Lob
    @Column(nullable = false)
    private String description; // 자세한 설명(본문)

    public static ProductRegistration create(
            BusinessInfo businessInfo,
            ProductCategory category,
            String productName,
            long price,
            int stockQuantity,
            int unitQuantity,
            String imageUrl,
            String descriptionTitle,
            String description
    ) {
        ProductRegistration product = new ProductRegistration();
        product.businessInfo = businessInfo;
        product.category = category;
        product.productName = productName;
        product.price = price;
        product.stockQuantity = stockQuantity;
        product.imageUrl = imageUrl;
        product.descriptionTitle = descriptionTitle;
        product.description = description;
        return product;
    }
}
