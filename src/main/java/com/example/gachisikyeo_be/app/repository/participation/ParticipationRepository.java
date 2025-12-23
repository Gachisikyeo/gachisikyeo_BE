package com.example.gachisikyeo_be.app.repository.participation;

import com.example.gachisikyeo_be.app.domain.participation.Participation;
import com.example.gachisikyeo_be.app.domain.participation.ParticipationStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    boolean existsByGroupPurchase_IdAndUser_Id(Long groupPurchaseId, Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select p
          from Participation p
          join fetch p.user u
          join fetch p.groupPurchase gp
          join fetch gp.product pr
         where p.id = :participationId
    """)
    Optional<Participation> findByIdForUpdate(@Param("participationId") Long participationId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update Participation p
           set p.status = :failed
         where p.status = :pending
           and p.createdAt <= :threshold
    """)
    int markFailedForExpiredPending(@Param("threshold") LocalDateTime threshold,
                                    @Param("pending") ParticipationStatus pending,
                                    @Param("failed") ParticipationStatus failed);
}
