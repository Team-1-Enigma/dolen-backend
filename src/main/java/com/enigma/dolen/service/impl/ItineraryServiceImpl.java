package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.dto.ItineraryDTO;
import com.enigma.dolen.model.dto.TripRequest;
import com.enigma.dolen.model.entity.Itinerary;
import com.enigma.dolen.model.entity.ItineraryDetail;
import com.enigma.dolen.model.entity.Trip;
import com.enigma.dolen.repository.ItineraryDetailRepository;
import com.enigma.dolen.repository.ItineraryRepository;
import com.enigma.dolen.service.ItineraryDetailService;
import com.enigma.dolen.service.ItineraryService;
import com.enigma.dolen.service.TripService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItineraryServiceImpl implements ItineraryService {

    private final ItineraryRepository itineraryRepository;
    private final TripService tripService;

    @Lazy
    private ItineraryDetailService itineraryDetailService;

    @Autowired
    @Lazy
    public void setItineraryDetailService(ItineraryDetailService itineraryDetailService) {
        this.itineraryDetailService = itineraryDetailService;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public List<ItineraryDTO> create(Trip trip, TripRequest tripRequest) {

        List<Itinerary> itineraries = new ArrayList<>();
        for (ItineraryDTO itineraryDTO : tripRequest.getItineraryDTOList()) {

            List<ItineraryDetail> itineraryDetails = new ArrayList<>();
            for (ItineraryDetail itineraryDetail : itineraryDTO.getItineraryDetails()) {
                itineraryDetails.add(itineraryDetailService.create(itineraryDetail));
            }

            Itinerary itinerary = Itinerary.builder()
                    .dayNumber(itineraryDTO.getDayNumber())
                    .trip(trip)
                    .itineraryDetails(itineraryDetails)
                    .build();

            Itinerary savedItinerary = itineraryRepository.saveAndFlush(itinerary);

            itineraries.add(savedItinerary);
        }

        return itineraries.stream().map(itinerary -> toItineraryDTO(itinerary)).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public ItineraryDTO addItinerary(ItineraryDTO itineraryDTO) {
        Trip existingTrip = tripService.getTripByIdForOther(itineraryDTO.getTripId());
        if (existingTrip == null) {
            return null;
        }
        List<ItineraryDetail> itineraryDetails = new ArrayList<>();
        for (ItineraryDetail itineraryDetail : itineraryDTO.getItineraryDetails()) {
            itineraryDetails.add(itineraryDetailService.create(itineraryDetail));
        }

        Itinerary itinerary = Itinerary.builder()
                .dayNumber(itineraryDTO.getDayNumber())
                .trip(existingTrip)
                .createdAt(LocalDateTime.now())
                .itineraryDetails(itineraryDetails)
                .build();

        itineraryRepository.saveAndFlush(itinerary);

        return toItineraryDTO(itinerary);
    }

    private ItineraryDTO toItineraryDTO(Itinerary savedItinerary) {
        return ItineraryDTO.builder()
                .id(savedItinerary.getId())
                .dayNumber(savedItinerary.getDayNumber())
                .itineraryDetails(savedItinerary.getItineraryDetails())
                .build();
    }

    @Override
    public Itinerary findById(String id) {
        return itineraryRepository.findById(id).orElse(null);
    }

    @Override
    public List<ItineraryDTO> getAllItineraryByTripId(String tripId) {
        Trip existingTrip = tripService.getTripByIdForOther(tripId);
        if (existingTrip == null) {
            return null;
        }
        List<ItineraryDTO> itineraryDTOList = existingTrip.getItineraries().stream()
                .map(itinerary -> toItineraryDTO(itinerary))
                .collect(Collectors.toList());

        return itineraryDTOList;
    }

    @Override
    public ItineraryDTO update(String id, ItineraryDTO itineraryDTO) {
        Trip existingTrip = tripService.getTripByIdForOther(itineraryDTO.getTripId());
        if (existingTrip == null) {
            return null;
        }
        Itinerary existingItinerary = itineraryRepository.findById(id).orElse(null);
        if (existingItinerary == null) {
            return null;
        }

        Itinerary newItinerary = Itinerary.builder()
                .id(existingItinerary.getId())
                .trip(existingTrip)
                .dayNumber(itineraryDTO.getDayNumber())
                .itineraryDetails(itineraryDTO.getItineraryDetails())
                .updatedAt(LocalDateTime.now())
                .build();
        itineraryRepository.saveAndFlush(newItinerary);

        return ItineraryDTO.builder()
                .id(newItinerary.getId())
                .dayNumber(newItinerary.getDayNumber())
                .itineraryDetails(newItinerary.getItineraryDetails())
                .build();
    }

    @Override
    public String delete(String id) {
        Itinerary existingItinerary = itineraryRepository.findById(id).orElse(null);
        if (existingItinerary == null) {
            return null;
        }
        itineraryRepository.deleteById(id);

        for(ItineraryDetail itineraryDetail : existingItinerary.getItineraryDetails()){
            itineraryDetailService.delete(itineraryDetail.getId());
        }

        return existingItinerary.getId();
    }
}
