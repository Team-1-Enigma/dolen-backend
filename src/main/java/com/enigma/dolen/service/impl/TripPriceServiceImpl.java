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
    public TripPriceResponse createTripPrice(Trip trip, TripPriceRequest tripPriceRequest) {
        Trip existingTrip = tripService.getTripByIdForOther(trip.getId());
        if (existingTrip == null){
            return null;
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

    @Override
    public TripPriceResponse addTripPrice(String tripId, TripPriceRequest tripPriceRequest) {
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
    public List<TripPriceResponse> getTripPriceByTripId(String tripId) {
        Trip existingTrip = tripService.getTripByIdForOther(tripId);
        if (existingTrip == null){
            return null;
        }
        if (existingTrip.getTripPrices().isEmpty()){
            return null;
        }


        List<TripPriceResponse> tripPriceResponses = existingTrip.getTripPrices().stream()
                .sorted(Comparator.comparing(TripPrice::getCreatedAt).reversed())
                .limit(1)
               .map(tripPrice -> toTripPriceResponse(tripPrice)).toList();

        return tripPriceResponses;
    }

    @Override
    public TripPriceDiscountResponse getTripPriceDiscount(String tripId) {
        Trip existingTrip = tripService.getTripByIdForOther(tripId);
        if(existingTrip == null){
            return null;
        }
        if (existingTrip.getTripPrices().isEmpty()){
            return null;
        }
        if(existingTrip.getTripPrices().size() == 1){
            return TripPriceDiscountResponse.builder()
                    .tripId(existingTrip.getId())
                    .priceBefore(existingTrip.getTripPrices().get(0).getPrice())
                    .build();
        }

        List<TripPrice> tripPrices = existingTrip.getTripPrices().stream()
                .sorted(Comparator.comparing(TripPrice::getCreatedAt).reversed())
                .limit(2).toList();

        double discount = (tripPrices.get(0).getPrice() > tripPrices.get(1).getPrice()) ? (((double) (tripPrices.get(0).getPrice() - tripPrices.get(1).getPrice()) / tripPrices.get(0).getPrice()) * 100.0) : (((double) (tripPrices.get(1).getPrice() - tripPrices.get(0).getPrice()) / tripPrices.get(1).getPrice()) * 100.0);

        return TripPriceDiscountResponse.builder()
                .tripId(existingTrip.getId())
                .priceBefore(tripPrices.get(1).getPrice())
                .priceAfter(tripPrices.get(0).getPrice())
                .discount((double) Math.round(discount * 100)/100)
                .build();
    }

    @Override
    public TripPriceResponse deleteTripPrice(String tripPriceId) {
        TripPrice existingTripPrice = tripPriceRepository.findById(tripPriceId).orElse(null);
        if (existingTripPrice == null) {
            return null;
        }
        existingTripPrice.setIsActive(false);
        tripPriceRepository.saveAndFlush(existingTripPrice);
        return toTripPriceResponse(existingTripPrice);
    }
}
