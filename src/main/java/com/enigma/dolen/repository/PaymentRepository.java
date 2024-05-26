package com.enigma.dolen.repository;

import com.enigma.dolen.model.dto.PaymentResponse;
import com.enigma.dolen.model.entity.Order;
import com.enigma.dolen.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, String> {
    Optional<Payment> getPaymentByOrder_Id(String orderId);
}
