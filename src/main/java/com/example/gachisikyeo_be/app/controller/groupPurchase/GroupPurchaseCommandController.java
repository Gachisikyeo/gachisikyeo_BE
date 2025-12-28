package com.example.gachisikyeo_be.app.controller.groupPurchase;

import com.example.gachisikyeo_be.app.dto.groupPurchase.CreateGroupPurchaseRequestDto;
import com.example.gachisikyeo_be.app.dto.groupPurchase.CreateGroupPurchaseResponseDto;
import com.example.gachisikyeo_be.app.dto.groupPurchase.GroupPurchaseDetailResponseDto;
import com.example.gachisikyeo_be.app.dto.groupPurchase.GroupPurchaseListItemResponseDto;
import com.example.gachisikyeo_be.app.service.groupPurchase.GroupPurchaseService;
import com.example.gachisikyeo_be.global.code.ErrorCode;
import com.example.gachisikyeo_be.global.code.SuccessCode;
import com.example.gachisikyeo_be.global.exception.BusinessException;
import com.example.gachisikyeo_be.global.responseTemplate.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name="GroupPurchaseCommand", description = "공동구매 생성/조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GroupPurchaseCommandController {
    private final GroupPurchaseService groupPurchaseService;

    @Operation(summary = "특정 상품의 공동구매 생성",
    description = "로그인한 사용자만 생성 가능, 공동구매 생성자가 해당 공동구매의 총대가 됨",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/products/{productId}/group-purchases")
    public ResponseEntity<ApiResponseTemplate<CreateGroupPurchaseResponseDto>> create(
            @PathVariable Long productId,
            @Valid @RequestBody CreateGroupPurchaseRequestDto req,
            Authentication authentication
    ){
        Long hostUserId = extractUserId(authentication);
        CreateGroupPurchaseResponseDto res = groupPurchaseService.create(hostUserId, productId, req);

        return ApiResponseTemplate.success(SuccessCode.GROUP_PURCHASE_CREATED, res);
    }

    @Operation(summary = "특정 상품의 공동구매 조회",
    description = "로그인 없이 조회 가능")
    @GetMapping("/products/{productId}/group-purchases")
    public ResponseEntity<ApiResponseTemplate<List<GroupPurchaseListItemResponseDto>>> listByProduct(
            @PathVariable Long productId
    ){
        List<GroupPurchaseListItemResponseDto> res = groupPurchaseService.listByProduct(productId);
        return ApiResponseTemplate.success(SuccessCode.GROUP_PURCHASE_LIST_FETCHED, res);
    }

    @Operation(summary = "공구 참여 상세 조회",
            description = "공구 조회")
    @GetMapping("/group-purchases/{groupPurchaseId}")
    public ResponseEntity<ApiResponseTemplate<GroupPurchaseDetailResponseDto>> getDetail(
            @PathVariable Long groupPurchaseId
    ) {
        GroupPurchaseDetailResponseDto res = groupPurchaseService.getDetail(groupPurchaseId);
        return ApiResponseTemplate.success(SuccessCode.GROUP_PURCHASE_DETAIL_FETCHED, res);
    }

    private Long extractUserId(Authentication authentication) {
        if (authentication == null || authentication.getName() == null || authentication.getName().isBlank()) {
            throw new BusinessException(ErrorCode.AUTH_REQUIRED);
        }
        try {
            return Long.valueOf(authentication.getName());
        } catch (NumberFormatException e) {
            throw new BusinessException(ErrorCode.AUTH_INVALID_PRINCIPAL);
        }
    }
}
