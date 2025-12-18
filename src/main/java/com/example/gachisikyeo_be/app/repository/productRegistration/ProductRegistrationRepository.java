package com.example.gachisikyeo_be.app.repository.productRegistration;

import com.example.gachisikyeo_be.app.domain.productRegistration.ProductRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRegistrationRepository
        extends JpaRepository<ProductRegistration, Long> {

    List<ProductRegistration> findByBusinessInfoId(Long businessInfoId);
}

