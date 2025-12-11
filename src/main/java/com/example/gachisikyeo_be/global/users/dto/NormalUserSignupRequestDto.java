package com.example.gachisikyeo_be.global.users.dto;

import com.example.gachisikyeo_be.global.users.domain.auth.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NormalUserSignupRequestDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank(message = "공백 없이 입력하시오")
    private String password;

    @NotBlank(message = "공백 없이 입력하시오")
    private String name;

    @NotBlank(message = "공백 없이 입력하시오")
    private String nickName;

    private UserType userType;

    private Long lawDongId;
}
