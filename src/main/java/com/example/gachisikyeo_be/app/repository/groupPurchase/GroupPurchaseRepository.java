package com.example.gachisikyeo_be.app.repository.groupPurchase;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchase;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupPurchaseRepository extends JpaRepository<GroupPurchase, Long> {
    @EntityGraph(attributePaths = {"hostUser", "region"})
    List<GroupPurchase> findByProductIdOrderByCreatedAtDesc(Long productId);
}
