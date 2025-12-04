package com.example.gachisikyeo_be.global.users.service;

import com.example.gachisikyeo_be.global.users.domain.auth.RefreshToken;
import com.example.gachisikyeo_be.global.users.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void saveOrUpdate(Long userId, String token) {
        refreshTokenRepository.findByUserId(userId)
                .ifPresentOrElse(
                        refreshToken -> refreshToken.updateToken(token),
                        () -> refreshTokenRepository.save(
                                RefreshToken.builder()
                                        .userId(userId)
                                        .token(token)
                                        .build()
                        )
                );
    }

    public String findTokenByUserId(Long userId) {
        return refreshTokenRepository.findByUserId(userId)
                .map(RefreshToken::getToken)
                .orElse(null);
    }

    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
