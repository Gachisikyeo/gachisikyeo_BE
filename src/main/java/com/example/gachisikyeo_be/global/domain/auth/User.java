package com.example.gachisikyeo_be.global.domain.auth;

import com.example.gachisikyeo_be.app.domain.region.LawDong;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "law_dong_id")
    private LawDong lawDong;

    // 찜, 쿠폰 필드 추가해야 함

    public static User createSocialUser(
            String email,
            String name,
            AuthProvider provider,
            String providerId
    ) {
        return User.builder()
                .email(email)
                // 실제로 로그인에 비밀번호를 쓰지 않으므로 더미 값
                .password("SOCIAL_LOGIN_USER")
                .name(name)
                .role(Role.USER) // 기본 ROLE_USER
                .provider(provider)
                .providerId(providerId)
                .build();
    }
}
