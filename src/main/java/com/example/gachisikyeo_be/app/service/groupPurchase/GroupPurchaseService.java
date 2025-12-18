package com.example.gachisikyeo_be.app.service.groupPurchase;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchase;
import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchaseCreateCommand;
import com.example.gachisikyeo_be.app.domain.region.LawDong;
import com.example.gachisikyeo_be.app.dto.groupPurchase.CreateGroupPurchaseRequestDto;
import com.example.gachisikyeo_be.app.dto.groupPurchase.CreateGroupPurchaseResponseDto;
import com.example.gachisikyeo_be.app.repository.groupPurchase.GroupPurchaseRepository;
import com.example.gachisikyeo_be.app.repository.region.LawDongRepository;
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

    @Transactional
    public CreateGroupPurchaseResponseDto create(Long hostUserId, CreateGroupPurchaseRequestDto req) {

        User host = userRepository.findById(hostUserId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 사용자입니다. userId=" + hostUserId));

        if (host.getUserType() != UserType.BUYER) {
            throw new IllegalStateException("BUYER만 공구를 생성할 수 있습니다.");
        }

        LawDong region = lawDongRepository.findById(req.getRegionId())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 지역입니다. regionId=" + req.getRegionId()));

        GroupPurchaseCreateCommand cmd = GroupPurchaseCreateCommand.builder()
                .productId(req.getProductId())
                .hostBuyQuantity(req.getHostBuyQuantity())
                .targetQuantity(req.getTargetQuantity())
                .minimumOrderUnit(req.getMinimumOrderUnit())
                .groupEndAt(req.getGroupEndAt())
                .pickupLocation(req.getPickupLocation())
                .pickupAt(req.getPickupAt())
                .build();

        GroupPurchase saved = groupPurchaseRepository.save(GroupPurchase.create(host, region, cmd));

        return CreateGroupPurchaseResponseDto.builder()
                .groupPurchaseId(saved.getId())
                .currentQuantity(saved.getCurrentQuantity())
                .groupEndAt(saved.getGroupEndAt())
                .userNickName(saved.getHostUser().getNickName())
                .regionId(saved.getRegion().getId())
                .targetQuantity(saved.getTargetQuantity())
                .currentQuantity(saved.getCurrentQuantity())
                .build();
    }
}
