package com.example.gachisikyeo_be.app.repository.productRegistration;

import com.example.gachisikyeo_be.app.domain.businessInfo.BusinessInfo;
import com.example.gachisikyeo_be.app.domain.productRegistration.Product;
import com.example.gachisikyeo_be.app.domain.productRegistration.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRegistrationRepository
        extends JpaRepository<Product, Long> {

    List<Product> findByBusinessInfo(BusinessInfo businessInfo);
    List<Product> findAllByOrderByViewCountDesc();
    List<Product> findByCategoryOrderByCreatedAtDesc(ProductCategory category);

}
