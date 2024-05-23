package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.CreateOrderRequest;
import com.enigma.dolen.model.dto.OrderResponse;

public interface OrderService {
    OrderResponse createOrder (CreateOrderRequest orderRequest);
}
