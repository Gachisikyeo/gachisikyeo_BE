package com.example.gachisikyeo_be.app.service.businessInfo;

import com.example.gachisikyeo_be.app.domain.businessInfo.BusinessInfo;
import com.example.gachisikyeo_be.app.dto.businessInfo.BusinessInfoRequest;
import com.example.gachisikyeo_be.app.repository.businessInfo.BusinessInfoRepository;
import com.example.gachisikyeo_be.global.users.domain.auth.User;
import com.example.gachisikyeo_be.global.users.domain.auth.UserType;
import com.example.gachisikyeo_be.global.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class BusinessInfoService {

    private final BusinessInfoRepository businessInfoRepository;
    private final UserRepository userRepository;

    public BusinessInfo create(Long userId,
                               BusinessInfoRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "유저 없음"
                        )
                );

        if (user.getUserType() != UserType.SELLER) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "SELLER만 등록 가능"
            );
        }

        return businessInfoRepository.findByUser(user)
                .orElseGet(() -> {
                    BusinessInfo businessInfo = new BusinessInfo(
                            user,
                            request.getBusinessNumber(),
                            request.getStoreName(),
                            request.getCeoName(),
                            request.getAddress()
                    );
                    return businessInfoRepository.save(businessInfo);
                });
    }
}
