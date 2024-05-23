package com.enigma.dolen.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TripPriceDiscountResponse {
    private String tripId;
    private Long priceBefore;
    private Long priceAfter;

    private Integer discount;
}
