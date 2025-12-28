package com.example.gachisikyeo_be.app.service.groupPurchase;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchase;
import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchaseCreateCommand;
import com.example.gachisikyeo_be.app.domain.productRegistration.Product;
import com.example.gachisikyeo_be.app.domain.region.LawDong;
import com.example.gachisikyeo_be.app.dto.groupPurchase.CreateGroupPurchaseRequestDto;
import com.example.gachisikyeo_be.app.dto.groupPurchase.CreateGroupPurchaseResponseDto;
import com.example.gachisikyeo_be.app.dto.groupPurchase.GroupPurchaseDetailResponseDto;
import com.example.gachisikyeo_be.app.dto.groupPurchase.GroupPurchaseListItemResponseDto;
import com.example.gachisikyeo_be.app.repository.groupPurchase.GroupPurchaseRepository;
import com.example.gachisikyeo_be.app.repository.productRegistration.ProductRegistrationRepository;
import com.example.gachisikyeo_be.app.repository.region.LawDongRepository;
import com.example.gachisikyeo_be.global.code.ErrorCode;
import com.example.gachisikyeo_be.global.exception.BusinessException;
import com.example.gachisikyeo_be.global.users.domain.auth.User;
import com.example.gachisikyeo_be.global.users.domain.auth.UserType;
import com.example.gachisikyeo_be.global.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;


@Service
@RequiredArgsConstructor
public class GroupPurchaseService {
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");
    private static final LocalTime END_OF_DAY = LocalTime.of(23, 59, 59);

    private final UserRepository userRepository;
    private final GroupPurchaseRepository groupPurchaseRepository;
    private final ProductRegistrationRepository productRegistrationRepository;

    @Transactional
    public CreateGroupPurchaseResponseDto create(Long hostUserId, Long productId, CreateGroupPurchaseRequestDto req) {

        User host = userRepository.findById(hostUserId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_USER));

        if (host.getUserType() != UserType.BUYER) {
            throw new BusinessException(ErrorCode.GROUP_PURCHASE_CREATE_FORBIDDEN);
        }

        // 무결성 검증 추가
        validateCreateRequest(req);

        Product productRegistration = productRegistrationRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        // ✅ 프론트에서 받은 "마감일(LocalDate)" -> 서버에서 23:59:59(LocalDateTime)로 확정
        LocalDateTime endAt = toEndAt(req.getGroupEndAt());
        LocalDateTime pickupAtKst = req.getPickupAt()
                .atZoneSameInstant(KST)
                .toLocalDateTime();

        GroupPurchaseCreateCommand cmd = GroupPurchaseCreateCommand.builder()
                .productRegistration(productRegistration) // PathVariable에서 받은 값 사용
                .hostBuyQuantity(req.getHostBuyQuantity())
                .targetQuantity(req.getTargetQuantity())
                .minimumOrderUnit(req.getMinimumOrderUnit())
                .groupEndAt(endAt)
                .deliveryLocation(req.getDeliveryLocation())
                .pickupLocation(req.getPickupLocation())
                .pickupAt(pickupAtKst)
                .hostContact(req.getHostContact())
                .build();

        GroupPurchase saved = groupPurchaseRepository.save(GroupPurchase.create(host, cmd));

        // 응답은 from()으로 통일
        return CreateGroupPurchaseResponseDto.from(saved);
    }

    @Transactional(readOnly = true)
    public List<GroupPurchaseListItemResponseDto> listByProduct(Long productId) {
        return groupPurchaseRepository.findByProductIdOrderByCreatedAtDesc(productId)
                .stream()
                .map(GroupPurchaseListItemResponseDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public GroupPurchaseDetailResponseDto getDetail(Long groupPurchaseId) {
        GroupPurchase gp = groupPurchaseRepository.findDetailById(groupPurchaseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_PURCHASE_NOT_FOUND));

        return GroupPurchaseDetailResponseDto.from(gp);
    }

    private LocalDateTime toEndAt(LocalDate endDate) {
        if (endDate == null) {
            // @NotNull로 걸러지겠지만 방어적으로 처리
            throw new BusinessException(ErrorCode.VALIDATION_EXCEPTION);
        }
        return endDate.atTime(END_OF_DAY); // ✅ 23:59:59
    }

    private void validateCreateRequest(CreateGroupPurchaseRequestDto req) {
        int hostBuy = req.getHostBuyQuantity();
        int target = req.getTargetQuantity();
        int minUnit = req.getMinimumOrderUnit();

        if (target < hostBuy) {
            throw new BusinessException(ErrorCode.GROUP_PURCHASE_INVALID_TARGET_QUANTITY);
        }

        if (minUnit > target) {
            throw new BusinessException(ErrorCode.GROUP_PURCHASE_INVALID_MINIMUM_ORDER_UNIT);
        }
        // ✅ 마감일(=해당일 23:59:59)이 "현재" 이후인지 검증
        LocalDateTime endAt = toEndAt(req.getGroupEndAt());
        LocalDateTime now = LocalDateTime.now(KST);

        if (!endAt.isAfter(now)) {
            throw new BusinessException(ErrorCode.GROUP_PURCHASE_INVALID_END_AT);
        }
    }
}
