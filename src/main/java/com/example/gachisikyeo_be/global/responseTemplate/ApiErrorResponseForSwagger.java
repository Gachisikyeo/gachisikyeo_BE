package com.example.gachisikyeo_be.global.responseTemplate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "공통 실패 응답 템플릿(ApiResponseTemplate의 error 형태)")
public class ApiErrorResponseForSwagger {
    @Schema(example = "404")
    private int status;

    @Schema(example = "false")
    private boolean success;

    @Schema(example = "찾는 지역 조회 실패")
    private String message;

    @Schema(description = "실패 응답에서는 보통 null", example = "null", nullable = true)
    private Object data;
}
