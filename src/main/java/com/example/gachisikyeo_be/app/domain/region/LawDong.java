package com.example.gachisikyeo_be.app.domain.region;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "law_dong",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_law_dong_law_code", columnNames = "law_code")
        })
public class LawDong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, nullable = false)
    private String lawCode;     // 법정동코드

    @Column(length = 50, nullable = false)
    private String sido;        // 시도명

    @Column(length = 50, nullable = false)
    private String sigungu;     // 시군구명

    @Column(length = 50, nullable = false)
    private String dong;        // 읍면동명 (리까지 쓰고 싶으면 여기 정책만 바꾸면 됨)

    private Boolean isActive;   // 삭제일자 없으면 true

    private String updateDate;  // 생성일자 또는 기준일자

    public void updateFrom(LawDong other) {
        this.sido = other.sido;
        this.sigungu = other.sigungu;
        this.dong = other.dong;
        this.isActive = other.isActive;
        this.updateDate = other.updateDate;
    }
}
