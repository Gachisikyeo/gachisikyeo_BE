package com.example.gachisikyeo_be.global.exception.handler;

import com.example.gachisikyeo_be.global.code.ErrorCode;
import com.example.gachisikyeo_be.global.exception.BusinessException;
import com.example.gachisikyeo_be.global.responseTemplate.ApiResponseTemplate;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * DTO Validation 실패 처리 (@RequestBody @Valid)
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
     * 요청 파라미터(@RequestParam/@PathVariable) Validation 실패 처리
     * 예: page < 0, size > MAX
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponseTemplate<Map<String, String>>> handleConstraintViolation(ConstraintViolationException e) {
        Map<String, String> errors = new LinkedHashMap<>();

        for (ConstraintViolation<?> v : e.getConstraintViolations()) {
            String path = v.getPropertyPath() == null ? "param" : v.getPropertyPath().toString();
            // "getAllProducts.page" 같은 형태면 마지막 토큰만 뽑아 "page"로 정리
            String key = path.contains(".") ? path.substring(path.lastIndexOf('.') + 1) : path;
            errors.put(key, v.getMessage());
        }

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
     * enum 파라미터 등 타입 변환 실패
     * 예: sortKey=CREATED, direction=DOWN 같은 값
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponseTemplate<Map<String, String>>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        Map<String, String> errors = new LinkedHashMap<>();
        String name = e.getName() == null ? "param" : e.getName();
        errors.put(name, "허용되지 않는 값입니다.");

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
