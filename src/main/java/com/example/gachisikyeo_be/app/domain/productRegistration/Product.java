package com.example.gachisikyeo_be.app.domain.productRegistration;

import com.example.gachisikyeo_be.app.domain.businessInfo.BusinessInfo;
import com.example.gachisikyeo_be.global.common.BaseTimeEntity;
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
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_info_id", nullable = false)
    private BusinessInfo businessInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category; //카테고리

    @Column(nullable = false)
    private String productName; //상품명

    @Column(nullable = false)
    private long price; //상품가격

    @Column(nullable = false)
    private int stockQuantity; //재고수량

    @Column(nullable = false)
    private int unitQuantity; //구성수량

    @Column(nullable = false)
    private long unitPrice; //개당가격

    @Column(nullable = false)
    private String imageUrl; //이미지 url

    @Column(nullable = false)
    private String descriptionTitle; // 설명제목

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description; //자세한 설명

    @Column(nullable = false)
    private long viewCount; // 조회수

    public static Product create(
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
        Product product = new Product();
        product.businessInfo = businessInfo;
        product.category = category;
        product.productName = productName;
        product.price = price;
        product.stockQuantity = stockQuantity;
        product.unitQuantity = unitQuantity;
        product.unitPrice = price / unitQuantity;
        product.imageUrl = imageUrl;
        product.descriptionTitle = descriptionTitle;
        product.description = description;
        product.viewCount = 0L;

        return product;
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void decreaseStockByOneBox() {
        if (this.stockQuantity <= 0) {
            throw new IllegalStateException("재고가 없습니다.");
        }
        this.stockQuantity -= 1;
    }


}
