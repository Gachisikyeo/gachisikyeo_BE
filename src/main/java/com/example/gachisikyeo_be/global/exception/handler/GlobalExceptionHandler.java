package com.example.gachisikyeo_be.global.exception.handler;

import com.example.gachisikyeo_be.global.code.ErrorCode;
import com.example.gachisikyeo_be.global.exception.BusinessException;
import com.example.gachisikyeo_be.global.responseTemplate.ApiResponseTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * DTO Validation 실패 처리
     * - field -> message 형태로 data에 담아 내려줌
     * - status/message는 ErrorCode.VALIDATION_EXCEPTION 기준으로 통일
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseTemplate<Map<String, String>>> handleValidation(MethodArgumentNotValidException e) {

        Map<String, String> errors = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(fe -> errors.put(fe.getField(), fe.getDefaultMessage()));

        ErrorCode ec = ErrorCode.VALIDATION_EXCEPTION;

        ApiResponseTemplate<Map<String, String>> body = ApiResponseTemplate.<Map<String, String>>builder()
                .status(ec.getHttpStatus().value())
                .success(false)
                .message(ec.getMessage())
                .data(errors)
                .build();

        return ResponseEntity.status(ec.getHttpStatus()).body(body);
    }

    /**
     * 비즈니스 예외 처리
     * - ErrorCode 기반 응답
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponseTemplate<Object>> handleBusiness(BusinessException e) {
        return ApiResponseTemplate.error(e.getErrorCode());
    }

    /**
     * 예상하지 못한 예외(서버 에러)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseTemplate<Object>> handleEtc(Exception e) {
        return ApiResponseTemplate.error(ErrorCode.INTERNAL_SERVER_EXCEPTION);
    }
}
