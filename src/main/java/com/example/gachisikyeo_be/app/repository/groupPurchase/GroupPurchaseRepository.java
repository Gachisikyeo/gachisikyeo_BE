package com.example.gachisikyeo_be.app.repository.groupPurchase;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchase;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupPurchaseRepository extends JpaRepository<GroupPurchase, Long> {
    // 목록에 필요한 연관관계(호스트, 상품, 지역) 미리 로딩
    @EntityGraph(attributePaths = {"host", "product", "region"})
    List<GroupPurchase> findAllByOrderByCreatedAtDesc();
}
