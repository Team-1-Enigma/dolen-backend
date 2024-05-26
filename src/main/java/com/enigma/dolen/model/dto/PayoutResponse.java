package com.enigma.dolen.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class PayoutResponse {

    @JsonProperty("payouts")
    private List<Payout> payouts;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder(toBuilder = true)
    public static class Payout {
        @JsonProperty("status")
        private String status;

        @JsonProperty("reference_no")
        private String referenceNo;
    }
}
