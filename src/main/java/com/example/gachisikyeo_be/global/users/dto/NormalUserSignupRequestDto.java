package com.example.gachisikyeo_be.global.users.dto;

import com.example.gachisikyeo_be.global.users.domain.auth.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "자체 로그인 요청 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NormalUserSignupRequestDto {
    @Schema(description = "이메일 주소", example = "bee@gmail.com")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "비밀번호", example = "LikeFlower*")
    @NotBlank(message = "공백 없이 입력하시오")
    private String password;

    @Schema(description = "본명", example = "호박벌")
    @NotBlank(message = "공백 없이 입력하시오")
    private String name;

    @Schema(description = "닉네임", example = "호호")
    @NotBlank(message = "공백 없이 입력하시오")
    private String nickName;

    @Schema(description = "유저 타입", example = "BUYER")
    private UserType userType;

    @Schema(description = "법정동 ID", example = "1")
    private String lawDongId;
}
