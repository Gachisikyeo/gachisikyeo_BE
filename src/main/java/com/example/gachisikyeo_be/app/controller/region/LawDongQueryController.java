package com.example.gachisikyeo_be.app.controller.region;

import com.example.gachisikyeo_be.app.service.region.LawDongService;
import com.example.gachisikyeo_be.global.code.SuccessCode;
import com.example.gachisikyeo_be.global.responseTemplate.ApiResponseTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name="LawDongQuery", description = "법정동 조회 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/law-dong")
public class LawDongQueryController {
    private final LawDongService lawDongService;

    /**
     * 시도 목록
     * GET /law-dong/sido
     */
    @Operation(summary = "시/도 조회", description = "전체 시/도 목록 조회")
    @ApiResponse(responseCode = "200", description = "시/도 목록 조회 성공")
    @GetMapping("/sido")
    public ResponseEntity<ApiResponseTemplate<List<String>>> getSidoList() {
        List<String> result = lawDongService.getSidoList();
        return ApiResponseTemplate.success(SuccessCode.LAWDONG_SIDO_LIST_SUCCESS, result);
    }

    /**
     * 시군구 목록
     * GET /law-dong/sigungu?sido=서울특별시
     */
    @Operation(summary = "시/군/구 조회", description = "선택한 시/도의 시/군/구 목록 조회")
    @ApiResponse(responseCode = "200", description = "시/군/구 목록 조회 성공")
    @GetMapping("/sigungu")
    public ResponseEntity<ApiResponseTemplate<List<String>>> getSigunguList(@RequestParam String sido) {
        List<String> result = lawDongService.getSigunguList(sido);
        return ApiResponseTemplate.success(SuccessCode.LAWDONG_SIGUNGU_LIST_SUCCESS, result);
    }

    /**
     * 동 목록
     * GET /law-dong/dong?sido=서울특별시&sigungu=구로구
     */
    @Operation(summary = "동 조회", description = "선택한 시/도와 시/군/구의 동 목록 조회")
    @ApiResponse(responseCode = "200", description = "동 목록 조회 성공")
    @GetMapping("/dong")
    public ResponseEntity<ApiResponseTemplate<List<String>>> getDongList(
            @RequestParam String sido,
            @RequestParam String sigungu
    ) {
        List<String> result = lawDongService.getDongList(sido, sigungu);
        return ApiResponseTemplate.success(SuccessCode.LAWDONG_DONG_LIST_SUCCESS, result);
    }

}
