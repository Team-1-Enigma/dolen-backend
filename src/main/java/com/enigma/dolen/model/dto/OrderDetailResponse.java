package com.enigma.dolen.model.dto;

import com.enigma.dolen.model.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class OrderDetailResponse {
    private String Id;

    private String participantName;

    private String contact;

    private Order order;

}
