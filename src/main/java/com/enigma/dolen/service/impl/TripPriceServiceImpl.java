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

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripPriceServiceImpl implements TripPriceService {

    private final TripPriceRepository tripPriceRepository;
    private final TripService tripService;
    @Override
    public TripPriceResponse createTripPrice(TripPriceRequest tripPriceRequest) {
        Trip existingTrip = tripService.getTripByIdForOther(tripPriceRequest.getTripId());
        if (existingTrip == null){
            return null;
        }
        TripPrice tripPrice;
        if (existingTrip.getTripPrices().size() == 0){
            tripPrice = TripPrice.builder()
                    .trip(existingTrip)
                    .price(tripPriceRequest.getPrice())
                    .quota(tripPriceRequest.getQuota())
                    .isActive(true)
                    .build();
            tripPriceRepository.saveAndFlush(tripPrice);
        }else{
            for (TripPrice value : existingTrip.getTripPrices()){
                if (value.getIsActive() == true){
                    value.setIsActive(false);
                    tripPriceRepository.saveAndFlush(value);
                }
            }
            tripPrice = TripPrice.builder()
                    .trip(existingTrip)
                    .price(tripPriceRequest.getPrice())
                    .quota(tripPriceRequest.getQuota())
                    .isActive(true)
                    .build();
            tripPriceRepository.saveAndFlush(tripPrice);
        }

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
        return List.of();
    }

    @Override
    public TripPriceDiscountResponse getTripPriceDiscount(String tripId) {
        return null;
    }
}
