package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.dto.*;
import com.enigma.dolen.model.entity.*;
import com.enigma.dolen.repository.TripPriceRepository;
import com.enigma.dolen.repository.TripRepository;
import com.enigma.dolen.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final TravelService travelService;
    private final LocationService locationService;

    @Lazy
    private ImageTripService imageTripService;

    @Lazy
    private ItineraryService itineraryService;

    @Lazy
    private TripPriceService tripPriceService;

    @Lazy
    private ItineraryDetailService itineraryDetailService;

    @Autowired
    @Lazy
    public void setImageTripService(ImageTripService imageTripService){
        this.imageTripService = imageTripService;
    }

    @Autowired
    @Lazy
    public void setItineraryService(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @Autowired
    @Lazy
    public void setTripPriceService(TripPriceService tripPriceService) {
        this.tripPriceService = tripPriceService;
    }

    @Autowired
    @Lazy
    public void setItineraryDetailService(ItineraryDetailService itineraryDetailService) {
        this.itineraryDetailService = itineraryDetailService;
    }

    @Override
    public Trip getTripByIdForOther(String id) {
        return tripRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public TripResponse createTrip(String travel_id, TripRequest tripRequest) {
        Travel existingTravel = travelService.getTravelByIdForOther(travel_id);
        if (existingTravel == null) {
            return null;
        }

        Location location = locationService.getOrSave(tripRequest.getLocationDTO());

        Trip trip = Trip.builder()
                .travel(existingTravel)
                .destination(tripRequest.getDestination())
                .departureDate(LocalDate.parse(tripRequest.getDepartureDate()))
                .returnDate(LocalDate.parse(tripRequest.getReturnDate()))
                .isActive(true)
                .location(location)
               .build();
        tripRepository.saveAndFlush(trip);

        List<ImageTripResponse> imageTripResponses = imageTripService.createImageTrip(trip, tripRequest);
        List<ItineraryDTO> itineraryDTOList = itineraryService.create(trip, tripRequest);
        TripPriceResponse tripPriceResponse = tripPriceService.createTripPrice(trip.getId(), tripRequest.getTripPriceRequest());

        return TripResponse.builder()
                .id(trip.getId())
                .travelId(existingTravel.getId())
                .destination(trip.getDestination())
                .locationDTO(LocationDTO.builder()
                        .city(location.getCity())
                        .province(location.getProvince())
                        .build())
                .departureDate(trip.getDepartureDate().toString())
                .returnDate(trip.getReturnDate().toString())
                .imageTripResponseList(imageTripResponses)
                .itineraryDTOList(itineraryDTOList)
                .tripPriceResponse(tripPriceResponse)
                .build();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public List<TripResponse> getTripByTravelId(String travelId) {
        Travel existingTravel = travelService.getTravelByIdForOther(travelId);
        if (existingTravel == null) {
            return null;
        }

        List<TripResponse> tripResponses = existingTravel.getTrips().stream()
                .filter(trip -> trip.getIsActive())
                .map((trip)-> {
                    TripPriceDiscountResponse tripPriceDiscountResponse = tripPriceService.getTripPriceDiscount(trip.getId());


                        return TripResponse.builder()
                                .id(trip.getId())
                                .travelId(existingTravel.getId())
                                .destination(trip.getDestination())
                                .locationDTO(LocationDTO.builder()
                                        .city(trip.getLocation().getCity())
                                        .province(trip.getLocation().getProvince())
                                        .build())
                                .departureDate(trip.getDepartureDate().toString())
                                .returnDate(trip.getReturnDate().toString())
                                .imageTripResponseList(trip.getImageTrips().stream()
                                        .filter(imageTrip -> imageTrip.getIsActive())
                                        .map(imageTrip -> ImageTripResponse.builder()
                                                .id(imageTrip.getId())
                                                .imageUrl(imageTrip.getImageUrl())
                                                .isActive(imageTrip.getIsActive())
                                                .build())
                                        .toList())
                                .itineraryDTOList(null)
                                .tripPriceResponse(null)
                                .tripPriceDiscountResponse(tripPriceDiscountResponse)
                                .build();
                    })
                .toList();

        return tripResponses;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public List<TripResponse> getAllTrip() {

        List<TripResponse> tripResponses = tripRepository.findAll().stream()
                .filter(trip -> trip.getIsActive())
                .map((trip)-> {
                    TripPriceDiscountResponse tripPriceDiscountResponse = tripPriceService.getTripPriceDiscount(trip.getId());


                    return TripResponse.builder()
                            .id(trip.getId())
                            .travelId(trip.getTravel().getId())
                            .destination(trip.getDestination())
                            .locationDTO(LocationDTO.builder()
                                    .city(trip.getLocation().getCity())
                                    .province(trip.getLocation().getProvince())
                                    .build())
                            .departureDate(trip.getDepartureDate().toString())
                            .returnDate(trip.getReturnDate().toString())
                            .imageTripResponseList(trip.getImageTrips().stream()
                                    .filter(imageTrip -> imageTrip.getIsActive())
                                    .map(imageTrip -> ImageTripResponse.builder()
                                            .id(imageTrip.getId())
                                            .imageUrl(imageTrip.getImageUrl())
                                            .isActive(imageTrip.getIsActive())
                                            .build())
                                    .toList())
                            .itineraryDTOList(null)
                            .tripPriceResponse(null)
                            .tripPriceDiscountResponse(tripPriceDiscountResponse)
                            .build();
                })
                .toList();

        return tripResponses;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public TripResponse updateTrip(String tripId, TripDTO tripDTO) {
        Travel existingTravel = travelService.getTravelByIdForOther(tripDTO.getTravelId());
        if (existingTravel == null) {
            return null;
        }
        Trip existingTrip = tripRepository.findById(tripId).orElse(null);
        if (existingTrip == null) {
            return null;
        }

        Trip trip = Trip.builder()
                .id(tripDTO.getId())
                .travel(existingTravel)
                .destination(tripDTO.getDestination())
                .departureDate(LocalDate.parse(tripDTO.getDepartureDate()))
                .returnDate(LocalDate.parse(tripDTO.getReturnDate()))
                .imageTrips(existingTrip.getImageTrips())
                .tripPrices(existingTrip.getTripPrices().stream()
                        .filter(tripPrice -> tripPrice.getIsActive())
                        .toList())
                .itineraries(existingTrip.getItineraries().stream()
                        .sorted(Comparator.comparing(Itinerary::getCreatedAt).reversed())
                        .limit(1).toList())
                .location(existingTrip.getLocation())
                .updatedAt(LocalDateTime.now())
                .build();

        return TripResponse.builder()
                .id(trip.getId())
                .travelId(existingTravel.getId())
                .destination(trip.getDestination())
                .locationDTO(LocationDTO.builder()
                        .city(trip.getLocation().getCity())
                        .province(trip.getLocation().getProvince())
                        .build())
                .departureDate(trip.getDepartureDate().toString())
                .returnDate(trip.getReturnDate().toString())
                .imageTripResponseList(null)
                .itineraryDTOList(null)
                .tripPriceResponse(null)
                .build();
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public String deleteTrip(String tripId) {
        Trip existingTrip = tripRepository.findById(tripId).orElse(null);
        if (existingTrip == null) {
            return null;
        }
        existingTrip.setIsActive(false);
        tripRepository.saveAndFlush(existingTrip);

        List<ImageTripResponse> imageTripResponses = new ArrayList<>();
        if (existingTrip.getImageTrips() != null) {
            existingTrip.getImageTrips().forEach(imageTrip -> {
                ImageTripResponse imageTripResponse = imageTripService.deleteImageTrip(imageTrip.getId());
                imageTripResponses.add(imageTripResponse);
            });
        }

        List<TripPriceResponse> tripPriceResponses = new ArrayList<>();
        if (existingTrip.getTripPrices() != null) {
            existingTrip.getTripPrices().stream().forEach(tripPrice -> {
                TripPriceResponse tripPriceResponse = tripPriceService.deleteTripPrice(tripPrice.getId());
                tripPriceResponses.add(tripPriceResponse);
            });
        }

        for(Itinerary itinerary : existingTrip.getItineraries()){
            for (ItineraryDetail itineraryDetail : itinerary.getItineraryDetails()){
                itineraryDetailService.delete(itineraryDetail.getId());
            }
            itineraryService.delete(itinerary.getId());
        }


        return existingTrip.getId();
    }


}
