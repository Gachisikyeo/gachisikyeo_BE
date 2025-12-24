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

    public MypageResponseDto getMypage(Long userId) {   //마이페이지 메인화면

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

    public CompletedGroupPurchaseDetailDto getCompletedDetail(  //완료된 공구 상세페이지
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
                gp.getProduct().getName(),  //상품명
                gp.getProduct().getImageUrl(),  //상품이미지
                gp.getTargetQuantity() * (int) gp.getProduct().getPrice(),  //상품총가격
                (int) gp.getProduct().getPrice(),   //상품 개당가격
                participation.getQuantity(),    //내가 구매한 수량
                participation.getId(),  //내 아이디
                participation.getUser().getNickName(),  //내 닉네임
                gp.getLeader().getUser().getNickName(), //총대 닉네임
                participation.getShareAmount(),
                gp.getPickupLocation(),
                gp.getPickupAt()
        );
    }

    private MypageGroupPurchaseDto toGroupPurchaseDto(Participation p) {
        return new MypageGroupPurchaseDto(
                p.getGroupPurchase().getId(),   //공구 아이디
                p.getGroupPurchase().getProduct().getName(),    //상품명
                p.getGroupPurchase().getProduct().getImageUrl(),    //상품 이미지
                (int) p.getGroupPurchase().getProduct().getPrice(), //상품 개당가격
                p.getGroupPurchase().getCreatedAt() //공구 생성 시간
        );
    }
}
