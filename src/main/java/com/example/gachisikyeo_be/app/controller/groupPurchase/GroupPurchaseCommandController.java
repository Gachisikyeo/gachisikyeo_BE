package com.example.gachisikyeo_be.app.controller.groupPurchase;

import com.example.gachisikyeo_be.app.dto.groupPurchase.CreateGroupPurchaseRequestDto;
import com.example.gachisikyeo_be.app.dto.groupPurchase.CreateGroupPurchaseResponseDto;
import com.example.gachisikyeo_be.app.service.groupPurchase.GroupPurchaseService;
import com.example.gachisikyeo_be.global.code.SuccessCode;
import com.example.gachisikyeo_be.global.responseTemplate.ApiResponseTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GroupPurchaseCommandController {
    private final GroupPurchaseService groupPurchaseService;

    @PostMapping("/products/{productId}/group-purchases")
    public ResponseEntity<ApiResponseTemplate<CreateGroupPurchaseResponseDto>> create(
            @PathVariable Long productId,
            @Valid @RequestBody CreateGroupPurchaseRequestDto req,
            Authentication authentication
    ){
        Long hostUserId = Long.valueOf(authentication.getName());
        CreateGroupPurchaseResponseDto res = groupPurchaseService.create(productId, hostUserId, req);

        return ApiResponseTemplate.success(SuccessCode.GROUP_PURCHASE_CREATED, res);
    }
}
