package com.example.gachisikyeo_be.app.controller.region;

import com.example.gachisikyeo_be.app.service.region.LawDongService;
import com.example.gachisikyeo_be.global.code.SuccessCode;
import com.example.gachisikyeo_be.global.responseTemplate.ApiResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/law-dong")
public class LawDongQueryController {
    private final LawDongService lawDongService;

    /**
     * 시도 목록
     * GET /law-dong/sido
     */
    @GetMapping("/sido")
    public ResponseEntity<ApiResponseTemplate<List<String>>> getSidoList() {
        List<String> result = lawDongService.getSidoList();
        return ApiResponseTemplate.success(SuccessCode.LAWDONG_SIDO_LIST_SUCCESS, result);
    }

    /**
     * 시군구 목록
     * GET /law-dong/sigungu?sido=서울특별시
     */
    @GetMapping("/sigungu")
    public ResponseEntity<ApiResponseTemplate<List<String>>> getSigunguList(@RequestParam String sido) {
        List<String> result = lawDongService.getSigunguList(sido);
        return ApiResponseTemplate.success(SuccessCode.LAWDONG_SIGUNGU_LIST_SUCCESS, result);
    }

    /**
     * 동 목록
     * GET /law-dong/dong?sido=서울특별시&sigungu=구로구
     */
    @GetMapping("/dong")
    public ResponseEntity<ApiResponseTemplate<List<String>>> getDongList(
            @RequestParam String sido,
            @RequestParam String sigungu
    ) {
        List<String> result = lawDongService.getDongList(sido, sigungu);
        return ApiResponseTemplate.success(SuccessCode.LAWDONG_DONG_LIST_SUCCESS, result);
    }

}
