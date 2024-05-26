package com.enigma.dolen.service;


import com.enigma.dolen.model.dto.OrderDetailRequest;
import com.enigma.dolen.model.dto.OrderDetailResponse;

public interface OrderDetailService {
    OrderDetailResponse createOrderDetail(OrderDetailRequest orderDetailRequest);
}
