package com.enigma.dolen.repository;

import com.enigma.dolen.model.dto.PaymentResponse;
import com.enigma.dolen.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    Payment getPaymentByOrder_Id(String orderId);
}
