package com.example.gachisikyeo_be.app.domain.region;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Seller {

    @Id
    @GeneratedValue
    private Long id;

    private Long userId; // 로그인한 사용자

    private String businessNumber;
    private String storeName;
    private String ceoName;
    private String address;

    @Enumerated(EnumType.STRING)
    private SellerStatus status;
}

