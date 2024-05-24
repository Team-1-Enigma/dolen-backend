package com.enigma.dolen.model.dto;

import com.enigma.dolen.model.entity.TransactionDetails;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
public class TransactionRequest {
    private TransactionDetails transaction_details;

}
