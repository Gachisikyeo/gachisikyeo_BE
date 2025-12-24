package com.example.gachisikyeo_be.global.exception;

import com.example.gachisikyeo_be.global.code.ErrorCode;
import lombok.Getter;

@Getter
public class LawDongNotFoundException extends RuntimeException{
    private final ErrorCode errorCode;

    public LawDongNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
