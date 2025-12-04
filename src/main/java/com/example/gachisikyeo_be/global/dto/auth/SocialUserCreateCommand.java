package com.example.gachisikyeo_be.global.dto.auth;

import com.example.gachisikyeo_be.global.domain.auth.AuthProvider;
import com.example.gachisikyeo_be.global.domain.auth.UserType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SocialUserCreateCommand {

    private final String email;
    private final String name;
    private final String nickName;

    private final AuthProvider provider;
    private final String providerId;

    private final UserType userType;
    // LawDong 은 엔티티라 여기서 안 들고 가고, 서비스에서 따로 찾아서 넘기는 걸로
}
