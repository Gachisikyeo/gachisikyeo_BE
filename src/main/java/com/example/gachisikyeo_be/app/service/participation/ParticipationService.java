package com.example.gachisikyeo_be.app.service.participation;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchase;
import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchaseStatus;
import com.example.gachisikyeo_be.app.domain.participation.Participation;
import com.example.gachisikyeo_be.app.dto.participation.CreateParticipationRequestDto;
import com.example.gachisikyeo_be.app.dto.participation.CreateParticipationResponseDto;
import com.example.gachisikyeo_be.app.repository.groupPurchase.GroupPurchaseRepository;
import com.example.gachisikyeo_be.app.repository.participation.ParticipationRepository;
import com.example.gachisikyeo_be.global.code.ErrorCode;
import com.example.gachisikyeo_be.global.exception.BusinessException;
import com.example.gachisikyeo_be.global.users.domain.auth.User;
import com.example.gachisikyeo_be.global.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ParticipationService {

    private final ParticipationRepository participationRepository;
    private final GroupPurchaseRepository groupPurchaseRepository;
    private final UserRepository userRepository;

    @Transactional
    public CreateParticipationResponseDto createPending(Long userId, Long groupPurchaseId, CreateParticipationRequestDto req) {

        User user = getUserOrThrow(userId);
        GroupPurchase gp = getGroupPurchaseOrThrow(groupPurchaseId);

        validateCreatable(gp, userId, req.getQuantity());

        int shareAmount = calculateShareAmount(gp, req.getQuantity());

        Participation saved = savePendingParticipation(gp, user, req, shareAmount);

        return CreateParticipationResponseDto.from(saved);
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));
    }

    private GroupPurchase getGroupPurchaseOrThrow(Long groupPurchaseId) {
        return groupPurchaseRepository.findById(groupPurchaseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_PURCHASE_NOT_FOUND));
    }

    private void validateCreatable(GroupPurchase gp, Long userId, int quantity) {
        validateGroupPurchaseOpen(gp); // 공구가 열렸는지
        validateNotEnded(gp); // 공구가 마감이 됐는지
        validateNotDuplicated(gp.getId(), userId); // 공구에 이미 참여를 했는지
        validateMinimumOrderUnit(gp, quantity); // 주문 최소 수량을 넘었는지
        validateNotExceedTarget(gp, quantity); // 목표 수량을 넘겼는지
    }

    private void validateGroupPurchaseOpen(GroupPurchase gp) {
        if (gp.getStatus() != GroupPurchaseStatus.OPEN) {
            throw new BusinessException(ErrorCode.GROUP_PURCHASE_NOT_OPEN);
        }
    }

    private void validateNotEnded(GroupPurchase gp) {
        if (gp.getGroupEndAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.GROUP_PURCHASE_ENDED);
        }
    }

    private void validateNotDuplicated(Long groupPurchaseId, Long userId) {
        if (participationRepository.existsByGroupPurchase_IdAndUser_Id(groupPurchaseId, userId)) {
            throw new BusinessException(ErrorCode.PARTICIPATION_ALREADY_EXISTS);
        }
    }

    private void validateMinimumOrderUnit(GroupPurchase gp, int quantity) {
        if (quantity < gp.getMinimumOrderUnit()) {
            throw new BusinessException(ErrorCode.INVALID_QUANTITY_MIN_ORDER_UNIT);
        }
    }

    private void validateNotExceedTarget(GroupPurchase gp, int quantity) {
        if (gp.getCurrentQuantity() + quantity > gp.getTargetQuantity()) {
            throw new BusinessException(ErrorCode.GROUP_PURCHASE_TARGET_EXCEEDED);
        }
    }

    private int calculateShareAmount(GroupPurchase gp, int quantity) {
        int unitPrice = extractUnitPriceOrThrow(gp);
        return unitPrice * quantity;
    }

    private int extractUnitPriceOrThrow(GroupPurchase gp) {
        // ProductRegistration 가격 필드/메서드명에 맞춰 수정
        int unitPrice = (int)gp.getProduct().getPrice();

        if (unitPrice <= 0) {
            throw new BusinessException(ErrorCode.PRODUCT_PRICE_NOT_SET);
        }
        return unitPrice;
    }

    private Participation savePendingParticipation(GroupPurchase gp, User user, CreateParticipationRequestDto req, int shareAmount) {
        Participation participation = Participation.create(
                gp,
                user,
                req.getQuantity(),
                req.getBuyerContact().trim(),
                shareAmount
        );
        return participationRepository.save(participation);
    }
}
