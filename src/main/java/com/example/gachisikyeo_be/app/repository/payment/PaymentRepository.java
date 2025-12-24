package com.example.gachisikyeo_be.app.repository.payment;

import com.example.gachisikyeo_be.app.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    boolean existsByParticipation_Id(Long participationId);
}
