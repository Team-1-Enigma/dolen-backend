package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.TripPriceDiscountResponse;
import com.enigma.dolen.model.dto.TripPriceRequest;
import com.enigma.dolen.model.dto.TripPriceResponse;
import com.enigma.dolen.model.entity.TripPrice;

import java.util.List;

public interface TripPriceService {
    TripPriceResponse createTripPrice(TripPriceRequest tripPriceRequest);
    List<TripPriceResponse> getTripPricebyTripId(String tripId);
    TripPriceDiscountResponse getTripPriceDiscount(String tripId);
}