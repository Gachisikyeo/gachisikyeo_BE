package com.example.gachisikyeo_be.app.domain.payment;

import com.example.gachisikyeo_be.app.domain.participation.Participation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "payments",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_payment_participation", columnNames = "participation_id")
        })
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "participation_id", nullable = false)
    private Participation participation;

    @Column(nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime paidAt;

    private Payment(Participation participation, int amount) {
        this.participation = participation;
        this.amount = amount;
        this.status = PaymentStatus.PAID; // 가짜 결제 = confirm 즉시 PAID
        this.createdAt = LocalDateTime.now();
        this.paidAt = LocalDateTime.now();
    }

    public static Payment paid(Participation participation, int amount) {
        return new Payment(participation, amount);
    }
}
