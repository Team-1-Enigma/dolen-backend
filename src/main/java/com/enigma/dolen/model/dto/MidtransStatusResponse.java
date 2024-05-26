package com.enigma.dolen.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class MidtransStatusResponse {
    private String approval_code;
    private String transaction_time;
    private String gross_amount;
    private String currency;
    private String order_id;
    private String payment_type;
    private String signature_key;
    private String status_code;
    private String transaction_id;
    private String transaction_status;
    private String fraud_status;
    private String expiry_time;
    private String settlement_time;
    private String status_message;
    private String merchant_id;
}
