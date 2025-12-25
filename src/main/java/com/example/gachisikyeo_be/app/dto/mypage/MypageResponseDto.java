package com.example.gachisikyeo_be.app.dto.mypage;

import com.example.gachisikyeo_be.app.domain.region.LawDong;
import com.example.gachisikyeo_be.global.users.domain.auth.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Schema(description = "마이페이지 응답 DTO")
@Getter
public class MypageResponseDto {

    @Schema(description = "사용자 닉네임")
    private final String nickname;

    @Schema(description = "사용자 이메일")
    private final String email;

    @Schema(description = "사용자 주소")
    private final String lawDong; //법정동

    @Schema(description = "사용자 유저 타입")
    private final String userType; //유저 타입 description


    @Schema(description = "완료된 공구 정보")
    private final Page<MypageGroupPurchaseDto> completedGroupPurchases;

    @Schema(description = "진행 중인 공구 정보")
    private final Page<MypageGroupPurchaseDto> ongoingGroupPurchases;

    public MypageResponseDto(
            String nickname, String email,
            String lawDong, String userType,
            Page<MypageGroupPurchaseDto> completedGroupPurchases,
            Page<MypageGroupPurchaseDto> ongoingGroupPurchases
    ) {
        this.nickname = nickname;
        this.email = email;
        this.lawDong = lawDong;
        this.userType = userType;
        this.completedGroupPurchases = completedGroupPurchases;
        this.ongoingGroupPurchases = ongoingGroupPurchases;
    }
}
