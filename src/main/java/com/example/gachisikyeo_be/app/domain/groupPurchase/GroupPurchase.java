package com.example.gachisikyeo_be.app.domain.groupPurchase;

import com.example.gachisikyeo_be.app.domain.region.LawDong;
import com.example.gachisikyeo_be.global.common.BaseTimeEntity;
import com.example.gachisikyeo_be.global.users.domain.auth.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "group_purchases")
public class GroupPurchase extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Product 엔티티가 아직 없으니 우선 FK 값만 보관
    @Column(name = "product_id", nullable = false)
    private Long productId;

    // 총대(=host). User와 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_user_id", nullable = false)
    private User hostUser;

    // 공구 진행 지역. LawDong와 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private LawDong region;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "target_quantity", nullable = false)
    private int targetQuantity;

    @Column(name = "minimum_order_unit", nullable = false)
    private int minimumOrderUnit;

    @Column(name = "current_quantity", nullable = false)
    private int currentQuantity;

    @Column(name = "group_end_at", nullable = false)
    private LocalDateTime groupEndAt;

    @Column(name = "pickup_location", nullable = false, length = 255)
    private String pickupLocation;

    @Column(name = "pickup_at", nullable = false)
    private LocalDateTime pickupAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GroupPurchaseStatus status;

    // 현재 단계에선 아직 안 쓰면 null 유지
    @Column(name = "settled_at")
    private LocalDateTime settledAt;

    public static GroupPurchase create(User hostUser, LawDong region, GroupPurchaseCreateCommand cmd) {
        return GroupPurchase.builder()
                .productId(cmd.getProductId())
                .hostUser(hostUser)
                .region(region)
                .title(cmd.getTitle())
                .targetQuantity(cmd.getTargetQuantity())
                .minimumOrderUnit(cmd.getMinimumOrderUnit())
                .currentQuantity(0)
                .groupEndAt(cmd.getGroupEndAt())
                .pickupLocation(cmd.getPickupLocation())
                .pickupAt(cmd.getPickupAt())
                .status(GroupPurchaseStatus.OPEN)
                .build();
    }
}
