package com.example.gachisikyeo_be.app.domain.groupPurchase;

import com.example.gachisikyeo_be.app.domain.participation.Participation;
import com.example.gachisikyeo_be.app.domain.productRegistration.Product;
import com.example.gachisikyeo_be.app.domain.region.LawDong;
import com.example.gachisikyeo_be.global.common.BaseTimeEntity;
import com.example.gachisikyeo_be.global.users.domain.auth.User;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // Product 엔티티가 아직 없으니 우선 FK 값만 보관   private Product product; 로 구현하셈
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    // 총대(=host). User와 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_user_id", nullable = false)
    private User hostUser;

    @Column(name = "host_contact", nullable = false)
    private String hostContact;

    // 총대가 구매하는 수량
    @Column(name = "host_buy_quantity", nullable = false)
    private int hostBuyQuantity;

    // 목표 수량
    @Column(name = "target_quantity", nullable = false)
    private int targetQuantity;

    // 최소 주문 수량
    @Column(name = "minimum_order_unit", nullable = false)
    private int minimumOrderUnit;

    // 현재 남은 수량
    /**
     * 현재 참여(주문) 수량 (누적)
     * - 초기: hostBuyQuantity
     * - 참가자가 늘면 증가
     */
    @Column(name = "current_quantity", nullable = false)
    private int currentQuantity;

    @Column(name = "group_end_at", nullable = false)
    private LocalDateTime groupEndAt;

    @Column(name = "delivery_location", nullable = false, length = 255)
    private String delivery_location;

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

    @OneToMany(mappedBy = "groupPurchase", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participation> participations = new ArrayList<>();

    public static GroupPurchase create(User hostUser, GroupPurchaseCreateCommand cmd) {
        return GroupPurchase.builder()
                .product(cmd.getProductRegistration())
                .hostUser(hostUser)
                .hostContact(cmd.getHostContact())
                .hostBuyQuantity(cmd.getHostBuyQuantity())
                .targetQuantity(cmd.getTargetQuantity())
                .minimumOrderUnit(cmd.getMinimumOrderUnit())
                .currentQuantity(cmd.getHostBuyQuantity()) // 매직넘버 제거, 초기값 일관성
                .groupEndAt(cmd.getGroupEndAt())
                .delivery_location(cmd.getDeliveryLocation())
                .pickupLocation(cmd.getPickupLocation())
                .pickupAt(cmd.getPickupAt())
                .status(GroupPurchaseStatus.OPEN)
                .build();
    }

    public boolean isTargetAchieved() {
        return currentQuantity >= targetQuantity;
    }

    public void increaseCurrentQuantity(int addQuantity) {
        this.currentQuantity += addQuantity;

        if (this.currentQuantity >= this.targetQuantity) {
            this.status = GroupPurchaseStatus.SUCCESS; // ✅ 즉시 SUCCESS
        }
    }
}
