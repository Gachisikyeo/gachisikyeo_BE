package com.example.gachisikyeo_be.global.users.service;

import com.example.gachisikyeo_be.app.domain.region.LawDong;
import com.example.gachisikyeo_be.app.repository.region.LawDongRepository;
import com.example.gachisikyeo_be.global.code.ErrorCode;
import com.example.gachisikyeo_be.global.exception.AuthException;
import com.example.gachisikyeo_be.global.jwt.TokenProvider;
import com.example.gachisikyeo_be.global.users.domain.auth.User;
import com.example.gachisikyeo_be.global.users.dto.LoginResponseDto;
import com.example.gachisikyeo_be.global.users.dto.NormalUserSignupRequestDto;
import com.example.gachisikyeo_be.global.users.dto.SocialSignupRequestDto;
import com.example.gachisikyeo_be.global.users.dto.auth.NormalUserCreateCommand;
import com.example.gachisikyeo_be.global.users.dto.auth.SocialUserCreateCommand;
import com.example.gachisikyeo_be.global.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final LawDongRepository lawDongRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto socialSignup(SocialSignupRequestDto request) {

        TokenProvider.Oauth2SignupPayload payload =
                tokenProvider.parseOauth2SignupToken(request.getOauth2SignupToken());

        String email = payload.getEmail();
        String name = payload.getName();

        var provider = payload.getProvider();
        String providerId = payload.getProviderId();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("이미 가입된 이메일입니다.");
        }

        LawDong lawDong = lawDongRepository.findById(request.getLawDongId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지역입니다."));

        // ✅ Command 객체로 감싸기
        SocialUserCreateCommand command = SocialUserCreateCommand.builder()
                .email(email)
                .name(name)
                .nickName(request.getNickName())
                .provider(provider)
                .providerId(providerId)
                .userType(request.getUserType())
                .build();

        User user = User.createSocialUser(command, lawDong);
        userRepository.save(user);

        String roleName = user.getRole().name();
        String accessToken = tokenProvider.createAccessToken(user.getId(), roleName);
        String refreshToken = tokenProvider.createRefreshToken(user.getId());
        refreshTokenService.saveOrUpdate(user.getId(), refreshToken);

        return LoginResponseDto.builder() // 추후 mapper 클래스를 활용예정.
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role("ROLE_" + roleName)
                .authProvider(user.getProvider())
                .userType(user.getUserType())
                .build();
    }

    @Transactional
    public void notSocialSignup(NormalUserSignupRequestDto normalUserSignupRequestDto){
        if(userRepository.findByEmail(normalUserSignupRequestDto.getEmail()).isPresent()){
            throw new AuthException(ErrorCode.ALREADY_EXIST_USER);
        }

        LawDong lawDong = lawDongRepository.findById(normalUserSignupRequestDto.getLawDongId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지역입니다."));

        NormalUserCreateCommand normalUserCreateCommand = NormalUserCreateCommand.builder()
                .email(normalUserSignupRequestDto.getEmail())
                .encodedPassword(passwordEncoder.encode(normalUserSignupRequestDto.getPassword()))
                .name(normalUserSignupRequestDto.getName())
                .nickName(normalUserSignupRequestDto.getNickName())
                .userType(normalUserSignupRequestDto.getUserType())
                .build();

        User user = User.createNotSocialUser(normalUserCreateCommand, lawDong);
        userRepository.save(user);
    }
}
