package com.example.gachisikyeo_be.app.service.sellerDashboard;

import com.example.gachisikyeo_be.app.domain.businessInfo.BusinessInfo;
import com.example.gachisikyeo_be.app.dto.sellerDashboard.SellerProductResponse;
import com.example.gachisikyeo_be.app.repository.businessInfo.BusinessInfoRepository;
import com.example.gachisikyeo_be.app.repository.productRegistration.ProductRegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerDashboardProductService {

    private final BusinessInfoRepository businessInfoRepository;
    private final ProductRegistrationRepository productRepository;

    public Page<SellerProductResponse> getMyProducts(
            Long sellerId,
            Pageable pageable
    ) {
        BusinessInfo businessInfo = businessInfoRepository
                .findByUser_Id(sellerId)
                .orElseThrow(() -> new IllegalStateException("사업자 정보 없음"));

        return productRepository
                .findByBusinessInfo(businessInfo, pageable)
                .map(SellerProductResponse::from);
    }
}
