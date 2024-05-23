package com.enigma.dolen.model.dto;

import com.enigma.dolen.model.entity.OrderDetail;
import com.enigma.dolen.model.entity.Trip;
import com.enigma.dolen.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CreateOrderRequest {
    private Integer quantity;
    private String userId;
    private String tripId;
    private List<OrderDetailRequest> orderDetailRequests;
}
