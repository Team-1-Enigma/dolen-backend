package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.dto.TripPriceDiscountResponse;
import com.enigma.dolen.model.dto.TripPriceRequest;
import com.enigma.dolen.model.dto.TripPriceResponse;
import com.enigma.dolen.model.entity.Trip;
import com.enigma.dolen.model.entity.TripPrice;
import com.enigma.dolen.repository.TripPriceRepository;
import com.enigma.dolen.service.TripPriceService;
import com.enigma.dolen.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TripPriceServiceImpl implements TripPriceService {

    private final TripPriceRepository tripPriceRepository;
    private final TripService tripService;
    @Override
    public TripPriceResponse createTripPrice(String tripId, TripPriceRequest tripPriceRequest) {
        Trip existingTrip = tripService.getTripByIdForOther(tripId);
        if (existingTrip == null){
            return null;
        }

        Optional<TripPrice> priceBefore = existingTrip.getTripPrices().stream()
                .filter(value -> value.getIsActive())
                .findFirst();
        if (priceBefore.isPresent()){
            priceBefore.get().setIsActive(false);
            tripPriceRepository.saveAndFlush(priceBefore.get());
        }
        TripPrice tripPrice = TripPrice.builder()
                .trip(existingTrip)
                .price(tripPriceRequest.getPrice())
                .quota(tripPriceRequest.getQuota())
                .isActive(true)
                .build();
        tripPriceRepository.saveAndFlush(tripPrice);

        return toTripPriceResponse(tripPrice);
    }

    private static TripPriceResponse toTripPriceResponse(TripPrice tripPrice) {
        return TripPriceResponse.builder()
                .id(tripPrice.getId())
                .price(tripPrice.getPrice())
                .quota(tripPrice.getQuota())
                .isActive(tripPrice.getIsActive())
                .createdAt(tripPrice.getCreatedAt())
                .build();
    }

    @Override
    public List<TripPriceResponse> getTripPricebyTripId(String tripId) {
        Trip existingTrip = tripService.getTripByIdForOther(tripId);
        if (existingTrip == null){
            return null;
        }

        List<TripPriceResponse> tripPriceResponses = existingTrip.getTripPrices().stream()
                .sorted(Comparator.comparing(TripPrice::getCreatedAt).reversed())
                .limit(2)
               .map(tripPrice -> toTripPriceResponse(tripPrice)).toList();

        return tripPriceResponses;
    }

    @Override
    public TripPriceDiscountResponse getTripPriceDiscount(String tripId) {
        Trip existingTrip = tripService.getTripByIdForOther(tripId);
        if(existingTrip == null){
            return null;
        }
        List<TripPrice> tripPrices = existingTrip.getTripPrices().stream()
                .sorted(Comparator.comparing(TripPrice::getCreatedAt).reversed())
                .limit(2).toList();

        Integer discount = Math.toIntExact((tripPrices.get(0).getPrice() - tripPrices.get(1).getPrice()) / tripPrices.get(0).getPrice());

        return TripPriceDiscountResponse.builder()
                .tripId(existingTrip.getId())
                .priceBefore(tripPrices.get(1).getPrice())
                .priceAfter(tripPrices.get(0).getPrice())
                .discount(discount)
                .build();
    }
}
