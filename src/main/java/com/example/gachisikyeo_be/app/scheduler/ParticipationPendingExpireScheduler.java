package com.example.gachisikyeo_be.app.scheduler;

import com.example.gachisikyeo_be.app.domain.participation.ParticipationStatus;
import com.example.gachisikyeo_be.app.repository.participation.ParticipationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParticipationPendingExpireScheduler {

    private final ParticipationRepository participationRepository;

    // 기본 10분 (원하면 yml로 조정)
    @Value("${participation.pending-expire-minutes:10}")
    private long expireMinutes;

    /**
     * 1분마다 오래된 PENDING 참여를 FAILED로 만료 처리
     */
    @Transactional
    @Scheduled(cron = "30 * * * * *") // 매 분 30초 (공구 마감 스케줄러와 초 분리)
    public void expirePendingParticipations() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(expireMinutes);

        int affected = participationRepository.markFailedForExpiredPending(
                threshold, ParticipationStatus.PENDING, ParticipationStatus.FAILED
        );

        if (affected > 0) {
            log.info("Expired pending participations. affected={}, threshold={}", affected, threshold);
        }
    }
}
