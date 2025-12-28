package com.example.gachisikyeo_be.app.repository.groupPurchase;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchase;
import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchaseStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupPurchaseRepository extends JpaRepository<GroupPurchase, Long> {
    @EntityGraph(attributePaths = {"hostUser"})
    List<GroupPurchase> findByProductIdOrderByCreatedAtDesc(Long productId);

    /*
    * flushAutomatically: 업데이트 전에 쌓인 변경사항을 flush
      clearAutomatically: 업데이트 후 영속성 컨텍스트를 비워서 다음 조회가 DB에서 최신값을 읽도록 보장
    *
    * */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update GroupPurchase gp
           set gp.status = :successStatus
         where gp.status = :openStatus
           and gp.groupEndAt <= :now
           and gp.currentQuantity >= gp.targetQuantity
    """)
    // 반환형이 int인 이유 -> 영향받은 행 수(row) 반환
    int markSuccessForEnded(@Param("now") LocalDateTime now,
                            @Param("openStatus") GroupPurchaseStatus openStatus,
                            @Param("successStatus") GroupPurchaseStatus successStatus);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update GroupPurchase gp
           set gp.status = :failedStatus
         where gp.status = :openStatus
           and gp.groupEndAt <= :now
           and gp.currentQuantity < gp.targetQuantity
    """)
    int markFailedForEnded(@Param("now") LocalDateTime now,
                           @Param("openStatus") GroupPurchaseStatus openStatus,
                           @Param("failedStatus") GroupPurchaseStatus failedStatus);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    select gp
      from GroupPurchase gp
      join fetch gp.product pr
     where gp.id = :groupPurchaseId
""")
    Optional<GroupPurchase> findByIdForUpdate(@Param("groupPurchaseId") Long groupPurchaseId);

    @Query("""
    select gp
      from GroupPurchase gp
      join fetch gp.product p
     where gp.id = :groupPurchaseId
""")
    Optional<GroupPurchase> findDetailById(@Param("groupPurchaseId") Long groupPurchaseId);
}
