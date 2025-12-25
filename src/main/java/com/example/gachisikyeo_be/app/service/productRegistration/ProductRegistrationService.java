package com.example.gachisikyeo_be.app.service.productRegistration;

import com.example.gachisikyeo_be.app.domain.businessInfo.BusinessInfo;
import com.example.gachisikyeo_be.app.domain.productRegistration.Product;
import com.example.gachisikyeo_be.app.domain.productRegistration.ProductCategory;
import com.example.gachisikyeo_be.app.dto.productRegistration.ProductRegistrationRequest;
import com.example.gachisikyeo_be.app.repository.businessInfo.BusinessInfoRepository;
import com.example.gachisikyeo_be.app.repository.productRegistration.ProductRegistrationRepository;
import com.example.gachisikyeo_be.app.service.awsS3.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductRegistrationService {

    private final ProductRegistrationRepository productRepository;
    private final BusinessInfoRepository businessInfoRepository;
    private final AwsS3Service awsS3Service;

    @Transactional
    public Product create(
            Long userId,
            ProductRegistrationRequest request,
            MultipartFile image
    ) {
        BusinessInfo businessInfo = businessInfoRepository
                .findByUser_Id(userId)
                .orElseThrow(() -> new IllegalStateException("사업자 정보 없음"));

        String imageUrl = awsS3Service.uploadSingleImage(image);

        Product product = Product.create(
                businessInfo,
                request.getCategory(),
                request.getProductName(),
                request.getPrice(),
                request.getStockQuantity(),
                request.getUnitQuantity(),
                imageUrl,
                request.getDescriptionTitle(),
                request.getDescription()
        );

        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Page<Product> getMyProducts(
            Long userId,
            Pageable pageable
    ) {
        BusinessInfo businessInfo = businessInfoRepository
                .findByUser_Id(userId)
                .orElseThrow(() -> new IllegalStateException("사업자 정보 없음"));

        return productRepository.findByBusinessInfo(businessInfo, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductsByCategory(
            ProductCategory category,
            Pageable pageable
    ) {
        return productRepository.findByCategory(category, pageable);
    }

    @Transactional
    public Product getProductDetail(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("상품 없음"));

        product.increaseViewCount();
        return product;
    }
}
