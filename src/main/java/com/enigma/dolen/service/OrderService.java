package com.enigma.dolen.service;

import com.enigma.dolen.constant.EStatus;
import com.enigma.dolen.model.dto.BeneficiariesResponse;
import com.enigma.dolen.model.dto.CreateOrderRequest;
import com.enigma.dolen.model.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder (CreateOrderRequest orderRequest);
    OrderResponse getOrderById(String orderId);
    List<OrderResponse> getOrderByUserId(String userId, EStatus eStatus);

    List<OrderResponse> getOrderByTripId(String tripId, EStatus eStatus);

    BeneficiariesResponse payout(String orderId);
}
