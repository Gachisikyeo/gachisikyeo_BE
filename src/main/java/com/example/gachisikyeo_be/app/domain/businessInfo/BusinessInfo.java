package com.example.gachisikyeo_be.app.domain.businessInfo;

import com.example.gachisikyeo_be.global.users.domain.auth.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class BusinessInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * SELLER 유저만 BusinessInfo를 가짐
     * BUYER 유저는 이 엔티티 자체가 없음
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private User user;

    private String businessNumber; // 사업자 등록 번호
    private String storeName;      // 상호명(법인명)
    private String ceoName;        // 대표자명
    private String address;        // 사업장 주소

    /**
     * BusinessInfo 생성자
     * - JPA가 아닌 서비스 계층에서 사용
     * - BusinessInfo는 반드시 User와 함께 생성됨
     */
    public BusinessInfo(User user,
                        String businessNumber,
                        String storeName,
                        String ceoName,
                        String address) {

        this.user = user;
        this.businessNumber = businessNumber;
        this.storeName = storeName;
        this.ceoName = ceoName;
        this.address = address;
    }
}
