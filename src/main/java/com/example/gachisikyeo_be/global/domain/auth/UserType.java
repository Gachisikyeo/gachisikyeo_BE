package com.example.gachisikyeo_be.global.domain.auth;

import lombok.Getter;

@Getter
public enum UserType {
    SELLER("사장님"),
    BUYER("일반 구매자");

    private final String description;

    UserType(String description){
        this.description = description;
    }
}
