package com.example.gachisikyeo_be.app.service.groupPurchase;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchase;
import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchaseCreateCommand;
import com.example.gachisikyeo_be.app.domain.productRegistration.Product;
import com.example.gachisikyeo_be.app.domain.region.LawDong;
import com.example.gachisikyeo_be.app.dto.groupPurchase.CreateGroupPurchaseRequestDto;
import com.example.gachisikyeo_be.app.dto.groupPurchase.CreateGroupPurchaseResponseDto;
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

import java.util.List;


@Service
@RequiredArgsConstructor
public class GroupPurchaseService {
    private final UserRepository userRepository;
    private final LawDongRepository lawDongRepository;
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

        LawDong region = lawDongRepository.findById(req.getRegionId())
                .orElseThrow(() -> new BusinessException(ErrorCode.REGION_NOT_FOUND));

        Product productRegistration = productRegistrationRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        GroupPurchaseCreateCommand cmd = GroupPurchaseCreateCommand.builder()
                .productRegistration(productRegistration) // PathVariable에서 받은 값 사용
                .hostBuyQuantity(req.getHostBuyQuantity())
                .targetQuantity(req.getTargetQuantity())
                .minimumOrderUnit(req.getMinimumOrderUnit())
                .groupEndAt(req.getGroupEndAt())
                .pickupLocation(req.getPickupLocation())
                .pickupAt(req.getPickupAt())
                .build();

        GroupPurchase saved = groupPurchaseRepository.save(GroupPurchase.create(host, region, cmd));

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
    }
}
