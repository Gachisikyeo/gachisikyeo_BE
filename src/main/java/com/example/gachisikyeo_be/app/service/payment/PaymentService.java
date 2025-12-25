package com.example.gachisikyeo_be.app.service.payment;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchase;
import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchaseStatus;
import com.example.gachisikyeo_be.app.domain.participation.Participation;
import com.example.gachisikyeo_be.app.domain.participation.ParticipationStatus;
import com.example.gachisikyeo_be.app.domain.payment.Payment;
import com.example.gachisikyeo_be.app.dto.payment.ConfirmPaymentResponseDto;
import com.example.gachisikyeo_be.app.repository.groupPurchase.GroupPurchaseRepository;
import com.example.gachisikyeo_be.app.repository.participation.ParticipationRepository;
import com.example.gachisikyeo_be.app.repository.payment.PaymentRepository;
import com.example.gachisikyeo_be.global.code.ErrorCode;
import com.example.gachisikyeo_be.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final ParticipationRepository participationRepository;
    private final GroupPurchaseRepository groupPurchaseRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public ConfirmPaymentResponseDto confirmDummyPayment(Long userId, Long participationId) {

        // 1) 참여 row 잠금 + gp/product fetch
        Participation participation = participationRepository.findByIdForUpdate(participationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PARTICIPATION_NOT_FOUND));

        // 2) 본인 참여인지
        if (!participation.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.PARTICIPATION_FORBIDDEN);
        }

        // 3) 상태가 PENDING인지
        if (participation.getStatus() != ParticipationStatus.PENDING) {
            throw new BusinessException(ErrorCode.PARTICIPATION_NOT_PENDING);
        }

        // 4) 공구 row 잠금 (동시 결제 확정 방지 핵심)
        Long gpId = participation.getGroupPurchase().getId();
        GroupPurchase gp = groupPurchaseRepository.findByIdForUpdate(gpId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_PURCHASE_NOT_FOUND));

        // 5) 공구 상태/마감 검증
        if (gp.getStatus() != GroupPurchaseStatus.OPEN) {
            throw new BusinessException(ErrorCode.GROUP_PURCHASE_NOT_OPEN);
        }
        LocalDateTime now = LocalDateTime.now();
        if (!gp.getGroupEndAt().isAfter(now)) { // endAt <= now
            throw new BusinessException(ErrorCode.GROUP_PURCHASE_ENDED);
        }

        // 6) 이미 결제 확정 되었는지 (낙관 + 유니크 제약으로 최종 방어)
        if (paymentRepository.existsByParticipation_Id(participationId)) {
            throw new BusinessException(ErrorCode.PAYMENT_ALREADY_CONFIRMED);
        }

        // 7) 목표 초과 방지 (확정 시점에 재검증)
        int qty = participation.getQuantity();
        if (gp.getCurrentQuantity() + qty > gp.getTargetQuantity()) {
            throw new BusinessException(ErrorCode.GROUP_PURCHASE_TARGET_EXCEEDED);
        }

        // 8) 금액 검증 (권장)
        int expected = (int) (gp.getProduct().getPrice() * qty);
        if (expected <= 0) throw new BusinessException(ErrorCode.PRODUCT_PRICE_NOT_SET);
        if (participation.getShareAmount() != expected) {
            throw new BusinessException(ErrorCode.PAYMENT_AMOUNT_MISMATCH);
        }

        // 9) 결제 기록 생성 + 참여 확정 + 공구 수량 반영(+즉시 SUCCESS)
        Payment payment;
        try {
            payment = paymentRepository.save(Payment.paid(participation, participation.getShareAmount()));
        } catch (DataIntegrityViolationException e) {
            // participation_id unique로 중복 결제 최종 차단
            throw new BusinessException(ErrorCode.PAYMENT_ALREADY_CONFIRMED);
        }

        participation.confirm();
        gp.increaseCurrentQuantity(qty); // ✅ current 증가 + target 달성 즉시 SUCCESS

        if (gp.getStatus() == GroupPurchaseStatus.SUCCESS) {
            gp.getProduct().decreaseStockByOneBox();
        }

        return ConfirmPaymentResponseDto.from(payment);
    }
}
