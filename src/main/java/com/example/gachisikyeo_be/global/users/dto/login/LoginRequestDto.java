package com.example.gachisikyeo_be.global.users.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "로그인 요청 DTO")
@Getter
@NoArgsConstructor
public class LoginRequestDto {
    @Schema(description = "이메일 주소", example = "bee@gmail.com")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "비밀번호", example = "LikeFlower*")
    @NotBlank
    private String password;
}
