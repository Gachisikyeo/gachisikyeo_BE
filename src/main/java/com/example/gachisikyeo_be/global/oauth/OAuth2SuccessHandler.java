//package com.example.gachisikyeo_be.global.oauth;
//
//import com.example.gachisikyeo_be.app.dto.LawDongDto;
//import com.example.gachisikyeo_be.global.jwt.TokenProvider;
//import com.example.gachisikyeo_be.global.responseTemplate.ApiResponseTemplate;
//import com.example.gachisikyeo_be.global.users.domain.auth.AuthProvider;
//import com.example.gachisikyeo_be.global.users.domain.auth.User;
//import com.example.gachisikyeo_be.global.users.domain.auth.UserType;
//import com.example.gachisikyeo_be.global.users.dto.login.LoginResponseDto;
//import com.example.gachisikyeo_be.global.users.repository.UserRepository;
//import com.example.gachisikyeo_be.global.users.service.RefreshTokenService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//import tools.jackson.databind.ObjectMapper;
//
//import java.io.IOException;
//import java.util.Map;
//
//@RequiredArgsConstructor
//@Component
//public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
//
//    private final TokenProvider tokenProvider;
//    private final RefreshTokenService refreshTokenService;
//    private final ObjectMapper objectMapper;
//    private final UserRepository userRepository;
//    @Override
//    public void onAuthenticationSuccess(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            Authentication authentication
//    ) throws IOException {
//
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//
//        // GoogleOAuth2UserService 에서 넣어준 값들
//        Long userId = oAuth2User.getAttribute("userId"); // 없으면 null
//        String email = oAuth2User.getAttribute("email");
//        String name = oAuth2User.getAttribute("name");
//        String nickName = oAuth2User.getAttribute("nickName");
//        AuthProvider authProvider = oAuth2User.getAttribute("provider"); // enum
//        String providerId = oAuth2User.getAttribute("sub"); // 구글 sub
//        UserType userType = oAuth2User.getAttribute("userType");
//
//        if (userId == null) {
//            // 🎯 1) 첫 소셜 로그인 → 추가 정보 필요
//
//            String oauth2SignupToken =
//                    tokenProvider.createOauth2SignupToken(email, name, authProvider, providerId);
//
//            // 프론트에 "추가입력 필요" 정보와 같이 전달
//            var body = ApiResponseTemplate.builder()
//                    .status(com.example.gachisikyeo_be.global.code.SuccessCode.USER_SOCIAL_NEED_ADDITIONAL_INFO.getHttpStatus().value())
//                    .success(true)
//                    .message("소셜 첫 로그인입니다. 추가 정보를 입력해 주세요.")
//                    .data(Map.of(
//                            "oauth2SignupToken", oauth2SignupToken,
//                            "email", email,
//                            "name", name,
//                            "provider", authProvider
//                    ))
//                    .build();
//
//            response.setStatus(com.example.gachisikyeo_be.global.code.SuccessCode.USER_SOCIAL_NEED_ADDITIONAL_INFO.getHttpStatus().value());
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().write(objectMapper.writeValueAsString(body));
//            return;
//        }
//
//        // 🎯 2) 이미 가입된 소셜 유저 → 기존 로직 그대로 JWT 발급
//        String roleName = (String) oAuth2User.getAttribute("role"); // "USER" or "ADMIN"
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalStateException("소셜 로그인 사용자를 찾을 수 없습니다. userId=" + userId));
//
//        // User 엔티티에서 LawDong 꺼내서 DTO로 변환
//        LawDongDto lawDongDto = LawDongDto.from(user.getLawDong());
//
//        // ✅ JWT 발급
//        String accessToken = tokenProvider.createAccessToken(userId, roleName);
//        String refreshToken = tokenProvider.createRefreshToken(userId);
//
//        // ✅ DB에 RefreshToken 저장/갱신
//        refreshTokenService.saveOrUpdate(userId, refreshToken);
//
//        // ✅ 기존 LoginResponseDto 재사용
//        LoginResponseDto loginResponse = LoginResponseDto.builder()
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .id(userId)
//                .email(email)
//                .name(name)
//                .nickName(nickName)
//                .role("ROLE_" + roleName) // 기존 로그인 응답과 포맷 맞춤
//                .authProvider(authProvider)
//                .userType(userType)
//                .lawDong(lawDongDto)
//                .build();
//
//        ApiResponseTemplate<LoginResponseDto> body =
//                ApiResponseTemplate.<LoginResponseDto>builder()
//                        .status(com.example.gachisikyeo_be.global.code.SuccessCode.USER_LOGIN_SUCCESS.getHttpStatus().value())
//                        .success(true)
//                        .message(com.example.gachisikyeo_be.global.code.SuccessCode.USER_LOGIN_SUCCESS.getMessage())
//                        .data(loginResponse)
//                        .build();
//
//        response.setStatus(com.example.gachisikyeo_be.global.code.SuccessCode.USER_LOGIN_SUCCESS.getHttpStatus().value());
//        response.setContentType("application/json;charset=UTF-8");
//        response.getWriter().write(objectMapper.writeValueAsString(body));
//    }
//}
package com.example.gachisikyeo_be.global.oauth;

import com.example.gachisikyeo_be.global.jwt.TokenProvider;
import com.example.gachisikyeo_be.global.users.domain.auth.AuthProvider;
import com.example.gachisikyeo_be.global.users.domain.auth.UserType;
import com.example.gachisikyeo_be.global.users.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Value("${app.front-base-url}")
    private String frontBaseUrl; // 예: https://gachisikyeo.com 또는 http://localhost:5173

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // GoogleOAuth2UserService(또는 Router)에서 넣어준 attribute들
        Long userId = oAuth2User.getAttribute("userId"); // 신규면 null
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        AuthProvider authProvider = oAuth2User.getAttribute("provider"); // enum
        String providerId = oAuth2User.getAttribute("sub"); // 구글 sub
        UserType userType = oAuth2User.getAttribute("userType"); // 있을 수도/없을 수도(신규면 null 가능)
        String roleName = oAuth2User.getAttribute("role"); // 기존유저면 "USER" 등

        // 1) 신규 소셜 로그인 → 추가정보 필요: 임시 토큰 발급 후 프론트로 redirect
        if (userId == null) {
            String oauth2SignupToken =
                    tokenProvider.createOauth2SignupToken(email, name, authProvider, providerId);

            String redirectUrl = frontBaseUrl + "/signup/google"
                    + "#oauth2SignupToken=" + enc(oauth2SignupToken)
                    + "&email=" + enc(email)
                    + "&name=" + enc(name)
                    + "&provider=" + enc(authProvider.name());

            response.sendRedirect(redirectUrl);
            return;
        }

        // 2) 기존 유저 → access/refresh 발급 후 프론트로 redirect
        String accessToken = tokenProvider.createAccessToken(userId, roleName);
        String refreshToken = tokenProvider.createRefreshToken(userId);

        refreshTokenService.saveOrUpdate(userId, refreshToken);

        String redirectUrl = frontBaseUrl + "/"
                + "#accessToken=" + enc(accessToken)
                + "&refreshToken=" + enc(refreshToken);

        response.sendRedirect(redirectUrl);
    }

    private String enc(String v) {
        if (v == null) return "";
        return URLEncoder.encode(v, StandardCharsets.UTF_8);
    }
}
