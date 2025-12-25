package com.example.gachisikyeo_be.global.users.domain.auth;

import lombok.Getter;

@Getter
public enum UserType {
    SELLER("사장님"),
    BUYER("구매자");

    private final String description;

    UserType(String description){
        this.description = description;
    }

    public String getDescription(){ return description; }   //마이페이지 유저 타입 조회 목적
}
