package com.example.gachisikyeo_be.global.jwt;

import com.example.gachisikyeo_be.global.users.domain.auth.AuthProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Collections;
import java.util.Date;

@Component
public class TokenProvider {

    private static final String ROLE_CLAIM = "Role";
    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";

    // ✅ 소셜 가입용 토큰 타입 구분용 (선택이지만 있으면 안전함)
    private static final String TOKEN_TYPE_CLAIM = "TokenType";
    private static final String TOKEN_TYPE_OAUTH2_SIGNUP = "OAUTH2_SIGNUP";

    private final SecretKey key;
    private final long accessTokenValidity;
    private final long refreshTokenValidity;

    public TokenProvider(
            @Value("${jwt.secret}") String secretKeyBase64,
            @Value("${jwt.access-token-validity-in-ms}") long accessTokenValidity,
            @Value("${jwt.refresh-token-validity-in-ms}") long refreshTokenValidity
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyBase64);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public String createAccessToken(Long userId, String roleName) {
        long now = System.currentTimeMillis();
        Date exp = new Date(now + accessTokenValidity);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim(ROLE_CLAIM, roleName) // ex) USER
                .expiration(exp)
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(Long userId) {
        long now = System.currentTimeMillis();
        Date exp = new Date(now + refreshTokenValidity);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .expiration(exp)
                .signWith(key)
                .compact();
    }

    // ✅ 소셜 회원가입용 임시 토큰 생성
    public String createOauth2SignupToken(
            String email,
            String name,
            AuthProvider provider,
            String providerId
    ) {
        long now = System.currentTimeMillis();
        // 필요하면 별도 유효기간 프로퍼티를 두고 바꿔도 됨 (지금은 accessTokenValidity 재사용)
        Date exp = new Date(now + accessTokenValidity);

        return Jwts.builder()
                .subject(email) // 기준은 이메일
                .claim("name", name)
                .claim("provider", provider.name())   // ex) GOOGLE
                .claim("providerId", providerId)      // ex) sub
                .claim(TOKEN_TYPE_CLAIM, TOKEN_TYPE_OAUTH2_SIGNUP)
                .expiration(exp)
                .signWith(key)
                .compact();
    }

    // ✅ 소셜 회원가입용 임시 토큰 파싱
    public Oauth2SignupPayload parseOauth2SignupToken(String token) {
        Claims claims = parseClaims(token);

        String tokenType = claims.get(TOKEN_TYPE_CLAIM, String.class);
        if (!TOKEN_TYPE_OAUTH2_SIGNUP.equals(tokenType)) {
            throw new IllegalArgumentException("소셜 회원가입용 토큰이 아닙니다.");
        }

        String email = claims.getSubject();
        String name = claims.get("name", String.class);
        String providerStr = claims.get("provider", String.class);
        String providerId = claims.get("providerId", String.class);

        AuthProvider provider = AuthProvider.valueOf(providerStr);

        return new Oauth2SignupPayload(email, name, provider, providerId);
    }

    // ✅ 소셜 가입용 토큰의 payload를 담기 위한 작은 DTO
    public static class Oauth2SignupPayload {
        private final String email;
        private final String name;
        private final AuthProvider provider;
        private final String providerId;

        public Oauth2SignupPayload(String email, String name, AuthProvider provider, String providerId) {
            this.email = email;
            this.name = name;
            this.provider = provider;
            this.providerId = providerId;
        }

        public String getEmail() {
            return email;
        }

        public String getName() {
            return name;
        }

        public AuthProvider getProvider() {
            return provider;
        }

        public String getProviderId() {
            return providerId;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String role = String.valueOf(claims.get(ROLE_CLAIM, Object.class)); // ex) USER

        return new UsernamePasswordAuthenticationToken(
                claims.getSubject(), "",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
        );
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(BEARER.length());
        }
        return null;
    }

    public String getUserIdFromToken(String token) {
        return parseClaims(token).getSubject();
    }
}
