package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.dto.PaymentRequest;
import com.enigma.dolen.model.dto.PaymentResponse;
import com.enigma.dolen.model.entity.Payment;
import com.enigma.dolen.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import com.enigma.dolen.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    @Override
    public PaymentResponse createPayment(PaymentRequest paymentRequest) {
        Payment payment = paymentRepository.save(
                Payment.builder()
                        .Id(paymentRequest.getPayment_id())
                        .paymentLink(paymentRequest.getPaymentLink())
                        .paymentStatus(paymentRequest.getPaymentStatus())
                        .total(paymentRequest.getTotal())
                        .order(paymentRequest.getOrder())
                        .build()
        );

        return PaymentResponse.builder()
                .Id(payment.getId())
                .paymentLink(payment.getPaymentLink())
                .paymentStatus(payment.getPaymentStatus())
                .total(payment.getTotal())
                .order(payment.getOrder())
                .build();
    }
}
