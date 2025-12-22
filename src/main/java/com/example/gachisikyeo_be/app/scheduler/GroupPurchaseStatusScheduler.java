package com.example.gachisikyeo_be.app.scheduler;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchaseStatus;
import com.example.gachisikyeo_be.app.repository.groupPurchase.GroupPurchaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupPurchaseStatusScheduler {
    private final GroupPurchaseRepository groupPurchaseRepository;
    /**
     * 1분마다 마감된 공구 상태 정리
     * - endAt 지남 + current >= target => SUCCESS
     * - endAt 지남 + current <  target => FAILED
     */
    @Transactional
    @Scheduled(cron = "0 * * * * *") // 매 분 0초
    public void finalizeEndedGroupPurchases() {
        LocalDateTime now = LocalDateTime.now();

        int success = groupPurchaseRepository.markSuccessForEnded(
                now, GroupPurchaseStatus.OPEN, GroupPurchaseStatus.SUCCESS
        );

        int failed = groupPurchaseRepository.markFailedForEnded(
                now, GroupPurchaseStatus.OPEN, GroupPurchaseStatus.FAILED
        );

        if (success > 0 || failed > 0) {
            log.info("Finalized group purchases. success={}, failed={}, now={}", success, failed, now);
        }
    }
}
