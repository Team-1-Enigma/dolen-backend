package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.dto.PaymentRequest;
import com.enigma.dolen.model.dto.PaymentResponse;
import com.enigma.dolen.model.entity.Payment;
import com.enigma.dolen.repository.PaymentRepository;
import com.enigma.dolen.service.MidtransService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import com.enigma.dolen.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final MidtransService midtransService;

    @Override
    public PaymentResponse createPayment(PaymentRequest paymentRequest) {

        PaymentResponse midtransCreatePaymentLink = midtransService.createTransaction(paymentRequest);

        Payment payment =
                Payment.builder()
                        .Id(midtransCreatePaymentLink.getId())
                        .paymentLink(midtransCreatePaymentLink.getPaymentLink())
                        .paymentStatus(midtransCreatePaymentLink.getPaymentStatus())
                        .total(midtransCreatePaymentLink.getTotal())
                        .order(paymentRequest.getOrder())
                        .build();
        paymentRepository.saveAndFlush(payment);

        return PaymentResponse.builder()
                .Id(payment.getId())
                .paymentLink(payment.getPaymentLink())
                .paymentStatus(payment.getPaymentStatus())
                .total(payment.getTotal())
                .order(payment.getOrder())
                .build();
    }

    @Override
    public PaymentResponse getPaymentByOrderId(String orderId) {
        Payment payment = paymentRepository.getPaymentByOrder_Id(orderId);

        return PaymentResponse.builder()
                .Id(payment.getId())
                .paymentLink(payment.getPaymentLink())
                .paymentStatus(payment.getPaymentStatus())
                .total(payment.getTotal())
                .order(payment.getOrder())
                .build();
    }

}
