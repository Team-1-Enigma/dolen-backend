package com.enigma.dolen.model.dto;

import com.enigma.dolen.model.entity.Trip;
import com.enigma.dolen.model.entity.User;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class OrderResponse {
    private String id;
    private Integer quantity;
    private User user;
    private TripResponse trip;
    private LocalTime order_date;
    private List<OrderDetailResponse> orderDetailResponses;

}
