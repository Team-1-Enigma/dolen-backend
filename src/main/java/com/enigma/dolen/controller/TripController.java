package com.enigma.dolen.controller;

import com.enigma.dolen.model.dto.*;
import com.enigma.dolen.model.entity.Trip;
import com.enigma.dolen.service.ItineraryService;
import com.enigma.dolen.service.ParticipantService;
import com.enigma.dolen.service.TripService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/travel")
public class TripController {

    private final TripService tripService;
    private final ItineraryService itineraryService;
    private final ParticipantService participantService;

    @PostMapping("/{travel_id}/trip")
    public ResponseEntity<?> createTrip(@PathVariable("travel_id") String travel_id, @ModelAttribute TripRequest tripRequest){

        System.out.println(travel_id);
        TripResponse tripResponse = tripService.createTrip(travel_id, tripRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                       .message("Trip created successfully")
                       .statusCode(HttpStatus.CREATED.value())
                       .data(tripResponse)
                       .build());
    }

    @GetMapping("/{id}/trips")
    public ResponseEntity<?> getTripByTravelId(@PathVariable String id){
        List<TripResponse> tripResponses = tripService.getTripByTravelId(id);

        return ResponseEntity.status(HttpStatus.OK)
               .body(CommonResponse.builder()
                       .message("Trip found")
                       .statusCode(HttpStatus.OK.value())
                       .data(tripResponses)
                       .build());
    }

    @GetMapping("/trips")
    public ResponseEntity<?> getAllTrip(){
        List<TripResponse> tripResponses = tripService.getAllTrip();

        return ResponseEntity.status(HttpStatus.OK)
               .body(CommonResponse.builder()
                       .message("Successfully get all trip")
                       .statusCode(HttpStatus.OK.value())
                       .data(tripResponses)
                       .build());
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<?> getById(@PathVariable String tripId){
        TripResponse tripResponse = tripService.getTripById(tripId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("Successfully get trip")
                        .statusCode(HttpStatus.OK.value())
                        .data(tripResponse)
                        .build());
    }

    @PutMapping("/trips/{id}")
    public ResponseEntity<?> updateTrip(@PathVariable String id, @ModelAttribute TripRequest tripRequest){
        TripResponse tripResponse = tripService.updateTrip(id, tripRequest);

        return ResponseEntity.status(HttpStatus.OK)
               .body(CommonResponse.builder()
                       .message("Trip updated")
                       .statusCode(HttpStatus.OK.value())
                       .data(tripResponse)
                       .build());
    }

    @DeleteMapping("/trips/{id}")
    public ResponseEntity<?> deleteTrip(@PathVariable String id){
        String data = tripService.deleteTrip(id);

        return ResponseEntity.status(HttpStatus.OK)
               .body(CommonResponse.builder()
                       .message("Trip deleted")
                       .statusCode(HttpStatus.OK.value())
                       .data(data)
                       .build());
    }

    @GetMapping("/{id}/itinerary")
    public ResponseEntity<?> getAllItinerariesByTripId(@PathVariable String id){
        List<ItineraryDTO> itineraryDTOList = itineraryService.getAllItineraryByTripId(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("Get All Itineraries by trip id")
                        .statusCode(HttpStatus.OK.value())
                        .data(itineraryDTOList)
                        .build());
    }

    @GetMapping("/{id}/participant")
    public ResponseEntity<?> getAllParticipantByTripId(@PathVariable String id){
        List<ParticipantDTO> participants = participantService.getAllParticipantByTripId(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("Get All Participant by trip id")
                        .statusCode(HttpStatus.OK.value())
                        .data(participants)
                        .build());
    }
}
