package com.example.gachisikyeo_be.app.repository.businessInfo;

import com.example.gachisikyeo_be.app.domain.businessInfo.BusinessInfo;
import com.example.gachisikyeo_be.global.users.domain.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessInfoRepository
        extends JpaRepository<BusinessInfo, Long> {

    boolean existsByUser(User user);
}
