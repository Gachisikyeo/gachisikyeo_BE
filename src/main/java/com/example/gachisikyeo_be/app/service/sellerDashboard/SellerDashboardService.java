package com.example.gachisikyeo_be.app.service.sellerDashboard;

import com.example.gachisikyeo_be.app.domain.businessInfo.BusinessInfo;
import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchase;
import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchaseStatus;
import com.example.gachisikyeo_be.app.domain.productRegistration.Product;
import com.example.gachisikyeo_be.app.repository.businessInfo.BusinessInfoRepository;
import com.example.gachisikyeo_be.app.repository.groupPurchase.GroupPurchaseRepository;
import com.example.gachisikyeo_be.app.repository.productRegistration.ProductRegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerDashboardService {

    private final BusinessInfoRepository businessInfoRepository;
    private final ProductRegistrationRepository productRepository;
    private final GroupPurchaseRepository groupPurchaseRepository;

    public long getTotalSoldQuantity(Long userId) {

        BusinessInfo businessInfo = businessInfoRepository.findByUser_Id(userId)
                .orElseThrow(() -> new IllegalStateException("사업자 정보 없음"));

        List<Product> products =
                productRepository.findByBusinessInfo(businessInfo);

        long total = 0;

        for (Product product : products) {
            List<GroupPurchase> groups =
                    groupPurchaseRepository.findByProductIdOrderByCreatedAtDesc(product.getId());

            for (GroupPurchase gp : groups) {
                if (gp.getStatus() == GroupPurchaseStatus.SUCCESS) {
                    total += gp.getCurrentQuantity();
                }
            }
        }

        return total;
    }
}
