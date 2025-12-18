package com.example.gachisikyeo_be.app.domain.groupPurchase;

import lombok.Getter;

@Getter
public enum GroupPurchaseStatus {
    OPEN("공구 모집중"),       // 모집중
    SUCCESS("공구 목표 달성"),    // 목표달성
    FAILED("마감 미달"),     // 마감 미달
    CANCELLED("공구 취소");   // 취소

    private final String description;

    GroupPurchaseStatus(String description){
        this.description = description;
    }
}
