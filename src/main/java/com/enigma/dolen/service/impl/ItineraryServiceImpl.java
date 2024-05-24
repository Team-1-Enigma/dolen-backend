package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.dto.ItineraryDTO;
import com.enigma.dolen.model.dto.ItineraryDetailDTO;
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

            Itinerary itinerary = Itinerary.builder()
                    .dayNumber(itineraryDTO.getDayNumber())
                    .trip(trip)
                    .itineraryDetails(new ArrayList<>())
                    .build();

            Itinerary savedItinerary = itineraryRepository.saveAndFlush(itinerary);

            List<ItineraryDetail> itineraryDetails = new ArrayList<>();
            for (ItineraryDetailDTO itineraryDetail : itineraryDTO.getItineraryDetailDTOList()) {
                ItineraryDetail saveItineraryDetail = itineraryDetailService.create(savedItinerary, itineraryDetail);

                itineraryDetails.add(saveItineraryDetail);
            }

            savedItinerary.setItineraryDetails(itineraryDetails);
            itineraryRepository.saveAndFlush(savedItinerary);

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

        Itinerary itinerary = Itinerary.builder()
                .dayNumber(itineraryDTO.getDayNumber())
                .trip(existingTrip)
                .itineraryDetails(new ArrayList<>())
                .build();

        Itinerary savedItinerary = itineraryRepository.saveAndFlush(itinerary);

        List<ItineraryDetail> itineraryDetails = new ArrayList<>();
        for (ItineraryDetailDTO itineraryDetail : itineraryDTO.getItineraryDetailDTOList()) {
            itineraryDetails.add(itineraryDetailService.create(savedItinerary, itineraryDetail));
        }
        savedItinerary.setItineraryDetails(itineraryDetails);
        itineraryRepository.saveAndFlush(savedItinerary);

        return toItineraryDTO(itinerary);
    }

    private ItineraryDTO toItineraryDTO(Itinerary savedItinerary) {
        return ItineraryDTO.builder()
                .id(savedItinerary.getId())
                .dayNumber(savedItinerary.getDayNumber())
                .itineraryDetailDTOList(savedItinerary.getItineraryDetails().stream()
                        .map(itineraryDetail -> ItineraryDetailDTO.builder()
                                .id(itineraryDetail.getId())
                                .startTime(String.valueOf(itineraryDetail.getStartTime()))
                                .endTime(String.valueOf(itineraryDetail.getEndTime()))
                                .activityDesc(itineraryDetail.getActivityDesc())
                                .build())
                        .toList())
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
                .itineraryDetails(itineraryDTO.getItineraryDetailDTOList().stream()
                        .map(itineraryDetailDTO -> ItineraryDetail.builder()
                                .itinerary(existingItinerary)
                                .startTime(LocalDateTime.parse(itineraryDetailDTO.getStartTime()))
                                .endTime(LocalDateTime.parse(itineraryDetailDTO.getEndTime()))
                                .activityDesc(itineraryDetailDTO.getActivityDesc())
                                .build())
                        .toList())
                .updatedAt(LocalDateTime.now())
                .build();
        itineraryRepository.saveAndFlush(newItinerary);

        return ItineraryDTO.builder()
                .id(newItinerary.getId())
                .dayNumber(newItinerary.getDayNumber())
                .itineraryDetailDTOList(newItinerary.getItineraryDetails().stream()
                        .map(itineraryDetail -> ItineraryDetailDTO.builder()
                                .id(itineraryDetail.getId())
                                .startTime(String.valueOf(itineraryDetail.getStartTime()))
                                .endTime(String.valueOf(itineraryDetail.getEndTime()))
                                .activityDesc(itineraryDetail.getActivityDesc())
                                .build())
                        .toList())
                .build();
    }

    @Override
    public List<ItineraryDTO> updateForTrip(Trip trip, TripRequest tripRequest) {
        for(Itinerary itinerary : trip.getItineraries()){
            for(ItineraryDetail itineraryDetail : itinerary.getItineraryDetails()){
                itineraryDetailService.delete(itineraryDetail.getId());
            }
            itineraryRepository.deleteById(itinerary.getId());
        }

        List<Itinerary> itineraries = new ArrayList<>();
        for (ItineraryDTO itineraryDTO : tripRequest.getItineraryDTOList()) {

            Itinerary itinerary = Itinerary.builder()
                    .dayNumber(itineraryDTO.getDayNumber())
                    .trip(trip)
                    .itineraryDetails(new ArrayList<>())
                    .build();

            Itinerary savedItinerary = itineraryRepository.saveAndFlush(itinerary);

            List<ItineraryDetail> itineraryDetails = new ArrayList<>();
            for (ItineraryDetailDTO itineraryDetail : itineraryDTO.getItineraryDetailDTOList()) {
                ItineraryDetail saveItineraryDetail = itineraryDetailService.create(savedItinerary, itineraryDetail);

                itineraryDetails.add(saveItineraryDetail);
            }

            savedItinerary.setItineraryDetails(itineraryDetails);
            itineraryRepository.saveAndFlush(savedItinerary);

            itineraries.add(savedItinerary);
        }

        return itineraries.stream().map(itinerary -> toItineraryDTO(itinerary)).collect(Collectors.toList());
    }

    @Override
    public String delete(String id) {
        Itinerary existingItinerary = itineraryRepository.findById(id).orElse(null);
        if (existingItinerary == null) {
            return null;
        }

        for(ItineraryDetail itineraryDetail : existingItinerary.getItineraryDetails()){
            itineraryDetailService.delete(itineraryDetail.getId());
        }
        itineraryRepository.deleteById(id);

        return existingItinerary.getId();
    }
}
