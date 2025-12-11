package com.example.gachisikyeo_be.global.users.domain.auth;

import com.example.gachisikyeo_be.app.domain.region.LawDong;
import com.example.gachisikyeo_be.global.users.dto.auth.NormalUserCreateCommand;
import com.example.gachisikyeo_be.global.users.dto.auth.SocialUserCreateCommand;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false, length = 120)
    private String password;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(nullable = false, length = 60)
    private String nickName;

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_ROLE", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    @Column(nullable = false)
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_TYPE", nullable = false)
    private UserType userType;

    @ManyToOne(fetch = FetchType.EAGER) // 추후 fetch 조인을 활용한 방법으로 EAGER -> LAZY로 변경해야 함, 지금은 프로젝트가 작아서 가능.
    @JoinColumn(name = "law_dong_id")
    private LawDong lawDong;

    // 찜, 쿠폰 필드 추가해야 함

    public static User createSocialUser(
            SocialUserCreateCommand socialUserCreateCommand,
            LawDong lawDong
    ) {
        return User.builder()
                .email(socialUserCreateCommand.getEmail())
                .password("SOCIAL_LOGIN_USER")   // 소셜은 더미 패스워드
                .name(socialUserCreateCommand.getName())
                .nickName(socialUserCreateCommand.getNickName())
                .role(Role.USER)
                .provider(socialUserCreateCommand.getProvider())
                .providerId(socialUserCreateCommand.getProviderId())
                .userType(socialUserCreateCommand.getUserType())
                .lawDong(lawDong)
                .build();
    }

    // 일반 로그인 정적 팩토리 메서드
    public static User createNotSocialUser(
            NormalUserCreateCommand normalUserCreateCommand,
            LawDong lawDong
    ){
        return User.builder()
                .email(normalUserCreateCommand.getEmail())
                .password(normalUserCreateCommand.getEncodedPassword())  // 암호화된 값 넣기
                .name(normalUserCreateCommand.getName())
                .nickName(normalUserCreateCommand.getNickName())
                .role(Role.USER)
                .provider(AuthProvider.LOCAL)
                .providerId(normalUserCreateCommand.getEmail())
                .userType(normalUserCreateCommand.getUserType())
                .lawDong(lawDong)
                .build();
    }
}
