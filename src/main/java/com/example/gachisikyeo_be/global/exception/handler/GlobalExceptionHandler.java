package com.example.gachisikyeo_be.global.exception.handler;

import com.example.gachisikyeo_be.global.code.ErrorCode;
import com.example.gachisikyeo_be.global.exception.BusinessException;
import com.example.gachisikyeo_be.global.responseTemplate.ApiResponseTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponseTemplate<Map<String, String>> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> errors = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(fe -> errors.put(fe.getField(), fe.getDefaultMessage()));

        return ApiResponseTemplate.<Map<String, String>>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .success(false)
                .message("요청 값 검증 실패")
                .data(errors)
                .build();
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponseTemplate<Object>> handleBusiness(BusinessException e) {
        ErrorCode ec = e.getErrorCode();
        return ApiResponseTemplate.error(ec);
    }
}
