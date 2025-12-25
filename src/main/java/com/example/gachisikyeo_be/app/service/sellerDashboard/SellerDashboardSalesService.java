package com.example.gachisikyeo_be.app.service.sellerDashboard;

import com.example.gachisikyeo_be.app.domain.participation.ParticipationStatus;
import com.example.gachisikyeo_be.app.repository.participation.ParticipationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SellerDashboardSalesService {

    private final ParticipationRepository participationRepository;

    public long getMonthlySales(Long sellerId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);

        LocalDateTime start = yearMonth
                .atDay(1)
                .atStartOfDay();

        LocalDateTime end = start.plusMonths(1);

        return participationRepository.sumMonthlySales(
                sellerId,
                ParticipationStatus.CONFIRMED,
                start,
                end
        );
    }

}
