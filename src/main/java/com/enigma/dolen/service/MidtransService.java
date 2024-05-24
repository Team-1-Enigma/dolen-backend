package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.PaymentRequest;
import com.enigma.dolen.model.dto.PaymentResponse;
import com.enigma.dolen.model.entity.Order;

public interface MidtransService {
    PaymentResponse createTransaction(PaymentRequest paymentRequest);
}
