package com.example.gachisikyeo_be.app.service.mypage;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchase;
import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchaseStatus;
import com.example.gachisikyeo_be.app.domain.participation.Participation;
import com.example.gachisikyeo_be.app.domain.region.LawDong;
import com.example.gachisikyeo_be.app.dto.common.SliceResponse;
import com.example.gachisikyeo_be.app.dto.mypage.CompletedGroupPurchaseDetailDto;
import com.example.gachisikyeo_be.app.dto.mypage.MyParticipationGroupPurchaseDto;
import com.example.gachisikyeo_be.app.dto.mypage.MyProfileResponseDto;
import com.example.gachisikyeo_be.app.repository.participation.ParticipationRepository;
import com.example.gachisikyeo_be.global.code.ErrorCode;
import com.example.gachisikyeo_be.global.exception.AuthException;
import com.example.gachisikyeo_be.global.exception.BusinessException;
import com.example.gachisikyeo_be.global.users.domain.auth.User;
import com.example.gachisikyeo_be.global.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MypageService {

    private final UserRepository userRepository;
    private final ParticipationRepository participationRepository;

    public MyProfileResponseDto getProfile(Long userId) {
        User user = getUserOrThrow(userId);

        LawDong lawDong = user.getLawDong();

        return new MyProfileResponseDto(
                user.getNickName(),
                user.getEmail(),
                lawDong.getDong(),
                user.getUserType().getDescription()
        );
    }

    public SliceResponse<MyParticipationGroupPurchaseDto> getMyParticipations(
            Long userId,
            GroupPurchaseStatus status,
            int page,
            int size
    ) {
        if (size <= 0) size = 3;
        Pageable pageable = PageRequest.of(page, size);

        Slice<Participation> slice =
                participationRepository.findSliceByUserAndGroupPurchaseStatus(userId, status, pageable);

        Slice<MyParticipationGroupPurchaseDto> mapped = slice.map(this::toItemDto);
        return SliceResponse.from(mapped);
    }

    public CompletedGroupPurchaseDetailDto getCompletedDetail(Long userId, Long participationId) {
        Participation participation =
                participationRepository.findByIdWithGroupAndProduct(participationId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.PARTICIPATION_NOT_FOUND));

        if (!participation.getUser().getId().equals(userId)) {
            throw new AuthException(ErrorCode.PARTICIPATION_FORBIDDEN);
        }

        GroupPurchase gp = participation.getGroupPurchase();

        return new CompletedGroupPurchaseDetailDto(
                gp.getProduct().getProductName(),
                gp.getProduct().getImageUrl(),
                (int) gp.getProduct().getPrice(),
                (int) gp.getProduct().getUnitPrice(),
                gp.getTargetQuantity(),
                gp.getPickupLocation(),
                gp.getPickupAt(),
                gp.getId(),
                participation.getUser().getNickName(),
                gp.getHostUser().getNickName(),
                participation.getQuantity() * (int) gp.getProduct().getUnitPrice()
        );
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(ErrorCode.NOT_FOUND_USER));
    }

    private MyParticipationGroupPurchaseDto toItemDto(Participation p) {
        GroupPurchase gp = p.getGroupPurchase();

        int unitPrice = (int) gp.getProduct().getUnitPrice();
        int myPayment = unitPrice * p.getQuantity();

        return new MyParticipationGroupPurchaseDto(
                p.getId(),
                gp.getId(),
                gp.getProduct().getProductName(),
                gp.getProduct().getImageUrl(),
                (int) gp.getProduct().getPrice(),
                unitPrice,
                gp.getTargetQuantity(),
                p.getQuantity(),
                myPayment,
                gp.getPickupLocation(),
                gp.getPickupAt(),
                gp.getStatus()
        );
    }
}
