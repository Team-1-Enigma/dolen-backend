package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.TripPriceDiscountResponse;
import com.enigma.dolen.model.dto.TripPriceRequest;
import com.enigma.dolen.model.dto.TripPriceResponse;
import com.enigma.dolen.model.entity.Trip;
import com.enigma.dolen.model.entity.TripPrice;

import java.util.List;

public interface TripPriceService {
    TripPriceResponse createTripPrice(Trip trip, TripPriceRequest tripPriceRequest);
    TripPriceResponse addTripPrice(String tripId, TripPriceRequest tripPriceRequest);
    List<TripPriceResponse> getTripPriceByTripId(String tripId);
    TripPriceDiscountResponse getTripPriceDiscount(String tripId);
    TripPriceResponse deleteTripPrice(String tripPriceId);
}
