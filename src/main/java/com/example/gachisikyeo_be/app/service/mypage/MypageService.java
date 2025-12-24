package com.example.gachisikyeo_be.app.service.mypage;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchase;
import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchaseStatus;
import com.example.gachisikyeo_be.app.domain.region.LawDong;
import com.example.gachisikyeo_be.app.dto.mypage.CompletedGroupPurchaseDetailDto;
import com.example.gachisikyeo_be.app.dto.mypage.MypageGroupPurchaseDto;
import com.example.gachisikyeo_be.app.dto.mypage.MypageResponseDto;
import com.example.gachisikyeo_be.global.code.ErrorCode;
import com.example.gachisikyeo_be.global.exception.AuthException;
import com.example.gachisikyeo_be.global.exception.BusinessException;
import com.example.gachisikyeo_be.global.users.domain.auth.User;
import com.example.gachisikyeo_be.global.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MypageService {

    private final UserRepository userRepository;
    private final ParticipationRepository participationRepository;

    public MypageResponseDto getMypage(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(ErrorCode.NOT_FOUND_USER));


        LawDong lawDong = user.getLawDong();

        String lawdong = lawDong == null ? null : lawDong.getDong();    //시도/시군구/동 다 뜨는 게 아니라 법정동만 떠야 함
        String userType = user.getUserType().getDescription();  //BUYER면 구매자, SELLER면 사장님

        List<MypageGroupPurchaseDto> completed =    //완료된 공구의 경우
                participationRepository
                        .findByUserAndGroupPurchaseStatus(
                                userId,
                                GroupPurchaseStatus.SUCCESS
                        )
                        .stream()
                        .map(this::toGroupPurchaseDto)
                        .toList();

        List<MypageGroupPurchaseDto> ongoing =    //참여 중인 공구의 경우
                participationRepository
                        .findByUserAndGroupPurchaseStatus(
                                userId,
                                GroupPurchaseStatus.OPEN
                        )
                        .stream()
                        .map(this::toGroupPurchaseDto)
                        .toList();

        return new MypageResponseDto(
                user.getNickName(),
                user.getEmail(),
                lawdong,
                userType,
                completed,
                ongoing
        );
    }

    public CompletedGroupPurchaseDetailDto getCompletedDetail(
            Long userId,
            Long participationId
    ) {
        Participation participation =
                participationRepository.findByIdWithFetch(participationId)
                        .orElseThrow(() ->
                                new BusinessException(ErrorCode.PARTICIPATION_NOT_FOUND));

        if (!participation.getUser().getId().equals(userId)) {
            throw new AuthException(ErrorCode.AUTH_FORBIDDEN);
        }
        GroupPurchase gp = participation.getGroupPurchase();

        return new CompletedGroupPurchaseDetailDto(
                gp.getProduct().getName(),
                gp.getProduct().getImageUrl(),
                gp.getTargetQuantity() * (int) gp.getProduct().getPrice(),
                (int) gp.getProduct().getPrice(),
                participation.getQuantity(),
                participation.getId(),
                participation.getUser().getNickName(),
                gp.getLeader().getUser().getNickName(),
                participation.getShareAmount()
        );
    }

    private MypageGroupPurchaseDto toGroupPurchaseDto(Participation p) {
        return new MypageGroupPurchaseDto(
                p.getGroupPurchase().getId(),
                p.getGroupPurchase().getProduct().getName(),
                p.getGroupPurchase().getProduct().getImageUrl(),
                (int) p.getGroupPurchase().getProduct().getPrice(),
                p.getGroupPurchase().getCreatedAt()
        );
    }
}
