package com.enigma.dolen.model.dto;

import com.enigma.dolen.constant.EPaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PaymentRequest {
    private String paymentLink;
    private Long total;
    private String payment_id;
    private EPaymentStatus paymentStatus;
}
