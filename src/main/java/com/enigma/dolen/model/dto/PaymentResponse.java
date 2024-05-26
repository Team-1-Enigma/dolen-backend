package com.enigma.dolen.model.dto;

import com.enigma.dolen.constant.EPaymentStatus;
import com.enigma.dolen.model.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PaymentResponse {
    private String Id;

    private String paymentLink;

    private Long total;

    private EPaymentStatus paymentStatus;

    private Order order;

}
