package com.example.gachisikyeo_be.app.service.mypage;

import com.example.gachisikyeo_be.app.domain.region.LawDong;
import com.example.gachisikyeo_be.app.dto.mypage.MypageResponseDto;
import com.example.gachisikyeo_be.global.code.ErrorCode;
import com.example.gachisikyeo_be.global.exception.AuthException;
import com.example.gachisikyeo_be.global.users.domain.auth.User;
import com.example.gachisikyeo_be.global.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MypageService {

    private final UserRepository userRepository;

    public MypageResponseDto getMypage(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(ErrorCode.NOT_FOUND_USER));


        LawDong lawDong = user.getLawDong();

        String lawdong = lawDong == null ? null : lawDong.getDong();    //시도/시군구/동 다 뜨는 게 아니라 법정동만 떠야 함
        String userType = user.getUserType().getDescription();  //BUYER면 구매자, SELLER면 사장님

        return new MypageResponseDto(
                user.getNickName(),
                user.getEmail(),
                lawdong,
                userType
        );
    }
}
