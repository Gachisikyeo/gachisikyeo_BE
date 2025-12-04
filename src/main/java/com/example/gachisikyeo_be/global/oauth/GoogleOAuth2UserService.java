package com.example.gachisikyeo_be.global.oauth;

import com.example.gachisikyeo_be.global.users.domain.auth.AuthProvider;
import com.example.gachisikyeo_be.global.users.domain.auth.User;
import com.example.gachisikyeo_be.global.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class GoogleOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        // 1) 구글에서 사용자 정보 조회
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String providerId = oAuth2User.getAttribute("sub"); // 구글 고유 ID

        if (email == null) {
            throw new OAuth2AuthenticationException("구글 계정에서 이메일을 가져올 수 없습니다.");
        }

        AuthProvider provider = AuthProvider.GOOGLE;

        // ✅ 2) DB 유저 "존재 여부만" 확인 (생성 X)
        User user = userRepository.findByEmail(email).orElse(null);

        // 3) SecurityContext 에 넣어줄 principal 구성
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());

        Collection<? extends GrantedAuthority> authorities;

        if (user != null) {
            // 이미 가입된 유저
            authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

            attributes.put("userId", user.getId());
            attributes.put("name", user.getName());
            attributes.put("role", user.getRole().name());      // "USER", "ADMIN"
            attributes.put("provider", user.getProvider());      // AuthProvider enum
            attributes.put("userType", user.getUserType());     // "SELLER", "BUYER"
        } else {
            // 처음 소셜 로그인 → 아직 우리 DB에는 없음
            authorities = List.of(new SimpleGrantedAuthority("ROLE_GUEST")); // 대충 임시 권한

            attributes.put("provider", provider);       // AuthProvider.GOOGLE
            attributes.put("providerId", providerId);   // sub
            attributes.put("name", name);
            // userId / role 없음
        }

        // key 를 "email" 로 사용 (getName() 할 때 사용)
        return new DefaultOAuth2User(authorities, attributes, "email");
    }
}
