package com.example.gachisikyeo_be.app.service.productRegistration;

import com.example.gachisikyeo_be.app.domain.businessInfo.BusinessInfo;
import com.example.gachisikyeo_be.app.domain.productRegistration.ProductRegistration;
import com.example.gachisikyeo_be.app.dto.request.ProductRegistrationRequest;
import com.example.gachisikyeo_be.app.repository.businessInfo.BusinessInfoRepository;
import com.example.gachisikyeo_be.app.repository.productRegistration.ProductRegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductRegistrationService {

    private final ProductRegistrationRepository productRepository;
    private final BusinessInfoRepository businessInfoRepository;

    @Transactional
    public ProductRegistration create(Long userId, ProductRegistrationRequest request) {
        BusinessInfo businessInfo = businessInfoRepository
                .findById(userId)
                .orElseThrow(() -> new IllegalStateException("사업자 정보 없음"));

        ProductRegistration product = ProductRegistration.create(
                businessInfo,
                request.getCategory(),
                request.getProductName(),
                request.getPrice(),
                request.getStockQuantity(),
                request.getUnitQuantity(),
                request.getImageUrl(),
                request.getDescriptionTitle(),
                request.getDescription()
        );

        return productRepository.save(product);
    }
}

