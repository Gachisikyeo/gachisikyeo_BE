package com.example.gachisikyeo_be.app.domain.participation;

import com.example.gachisikyeo_be.app.domain.groupPurchase.GroupPurchase;
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
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "participations",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_participation_group_user", columnNames = {"group_purchase_id", "user_id"})
        })
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_purchase_id", nullable = false)
    private GroupPurchase groupPurchase;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "buyer_contact", nullable = false, length = 30)
    private String buyerContact;

    @Column(name = "share_amount", nullable = false)
    private int shareAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ParticipationStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private Participation(GroupPurchase groupPurchase,
                          User user,
                          int quantity,
                          String buyerContact,
                          int shareAmount) {
        this.groupPurchase = groupPurchase;
        this.user = user;
        this.quantity = quantity;
        this.buyerContact = buyerContact;
        this.shareAmount = shareAmount;
        this.status = ParticipationStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public static Participation create(GroupPurchase groupPurchase,
                                       User user,
                                       int quantity,
                                       String buyerContact,
                                       int shareAmount) {
        return new Participation(groupPurchase, user, quantity, buyerContact, shareAmount);
    }

    public void cancel() {
        this.status = ParticipationStatus.CANCELED;
    }
}
