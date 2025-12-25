package com.example.gachisikyeo_be.app.repository.participation;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchaseStatus;
import com.example.gachisikyeo_be.app.domain.participation.Participation;
import com.example.gachisikyeo_be.app.domain.participation.ParticipationStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
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

    @Query(
            value = """
        select p
        from Participation p
        join fetch p.groupPurchase gp
        join fetch gp.product pr
        where p.user.id = :userId
          and gp.status = :status
        order by gp.createdAt asc
    """,
            countQuery = """
        select count(p)
        from Participation p
        join p.groupPurchase gp
        where p.user.id = :userId
          and gp.status = :status
    """
    )
    Page<Participation> findByUserAndGroupPurchaseStatus(
            @Param("userId") Long userId,
            @Param("status") GroupPurchaseStatus status,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {
            "groupPurchase",
            "groupPurchase.product",
            "groupPurchase.hostUser",
            "user"
    })
    @Query("""
        select p
        from Participation p
        join p.groupPurchase gp
        where p.user.id = :userId
          and gp.status = :status
        order by gp.createdAt desc
    """)
    Slice<Participation> findSliceByUserAndGroupPurchaseStatus(
            @Param("userId") Long userId,
            @Param("status") GroupPurchaseStatus status,
            Pageable pageable
    );

    // 결제 화면 조회용(읽기), 마이페이지 공구 조회하는 데도 사용함
    @Query("""
        select p
        from Participation p
        join fetch p.groupPurchase gp
        join fetch gp.product pr
        join fetch gp.hostUser hu
        join fetch p.user u
        where p.id = :participationId
    """)
    Optional<Participation> findByIdWithGroupAndProduct(@Param("participationId") Long participationId);
}
