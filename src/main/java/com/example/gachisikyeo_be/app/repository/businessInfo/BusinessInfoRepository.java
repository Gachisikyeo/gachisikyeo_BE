package com.example.gachisikyeo_be.app.repository.businessInfo;

import com.example.gachisikyeo_be.app.domain.businessInfo.BusinessInfo;
import com.example.gachisikyeo_be.global.users.domain.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessInfoRepository
        extends JpaRepository<BusinessInfo, Long> {

    // user 엔티티 기준
    boolean existsByUser(User user);

    Optional<BusinessInfo> findByUser(User user);

    // 🔥 userId 기준 (이게 핵심)
    Optional<BusinessInfo> findByUser_Id(Long userId);
}
