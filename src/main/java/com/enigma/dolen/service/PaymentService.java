package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.PaymentRequest;
import com.enigma.dolen.model.dto.PaymentResponse;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest paymentRequest);
    PaymentResponse getPaymentByOrderId(String orderId);
}
