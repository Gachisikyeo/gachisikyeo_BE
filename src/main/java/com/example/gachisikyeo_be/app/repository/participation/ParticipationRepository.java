package com.example.gachisikyeo_be.app.repository.participation;

import com.example.gachisikyeo_be.app.domain.participation.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    boolean existsByGroupPurchase_IdAndUser_Id(Long groupPurchaseId, Long userId);
}
