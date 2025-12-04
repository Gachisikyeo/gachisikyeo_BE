package com.example.gachisikyeo_be.global.users.dto.auth;

import com.example.gachisikyeo_be.global.users.domain.auth.UserType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NormalUserCreateCommand {
    private final String email;
    private final String name;
    private final String nickName;
    private final String encodedPassword;
    private final UserType userType;
}
