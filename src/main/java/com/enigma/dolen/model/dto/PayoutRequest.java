package com.enigma.dolen.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"payouts"})
public class PayoutRequest {
    @JsonProperty("payouts")
    private List<Payout> payouts;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder(toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "beneficiary_name",
            "beneficiary_account",
            "beneficiary_bank",
            "beneficiary_email",
            "amount",
            "notes"
    })
    public static class Payout {
        @JsonProperty("beneficiary_name")
        private String beneficiaryName;

        @JsonProperty("beneficiary_account")
        private String beneficiaryAccount;

        @JsonProperty("beneficiary_bank")
        private String beneficiaryBank;

        @JsonProperty("beneficiary_email")
        private String beneficiaryEmail;

        @JsonProperty("amount")
        private String amount;

        @JsonProperty("notes")
        private String notes;
    }
}
