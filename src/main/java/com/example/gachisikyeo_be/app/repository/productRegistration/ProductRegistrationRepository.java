package com.example.gachisikyeo_be.app.repository.productRegistration;

import com.example.gachisikyeo_be.app.domain.businessInfo.BusinessInfo;
import com.example.gachisikyeo_be.app.domain.productRegistration.Product;
import com.example.gachisikyeo_be.app.domain.productRegistration.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRegistrationRepository
        extends JpaRepository<Product, Long> {

    Page<Product> findByBusinessInfo(BusinessInfo businessInfo, Pageable pageable);

    List<Product> findByBusinessInfo(BusinessInfo businessInfo);

    Page<Product> findAll(Pageable pageable);

    Page<Product> findByCategory(ProductCategory category, Pageable pageable);
}
