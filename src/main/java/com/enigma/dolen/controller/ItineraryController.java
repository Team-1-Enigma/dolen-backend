package com.enigma.dolen.controller;

import com.enigma.dolen.model.dto.CommonResponse;
import com.enigma.dolen.model.dto.ItineraryDTO;
import com.enigma.dolen.service.ItineraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/travel/trips/itinerary")
public class ItineraryController {

    private final ItineraryService itineraryService;

    @PostMapping
    public ResponseEntity<?> addItinerary(@RequestBody ItineraryDTO itineraryDTO) {
        ItineraryDTO newItinerary = itineraryService.addItinerary(itineraryDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .message("Itinerary created successfully")
                        .statusCode(HttpStatus.CREATED.value())
                        .data(newItinerary)
                        .build());

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateItinerary(@PathVariable String id, @RequestBody ItineraryDTO itineraryDTO) {
        ItineraryDTO updatedItinerary = itineraryService.update(id, itineraryDTO);

        return ResponseEntity.status(HttpStatus.OK)
               .body(CommonResponse.builder()
                       .message("Itinerary updated")
                       .statusCode(HttpStatus.OK.value())
                       .data(updatedItinerary)
                       .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteItinerary(@PathVariable String id) {
        String data = itineraryService.delete(id);

        return ResponseEntity.status(HttpStatus.OK)
               .body(CommonResponse.builder()
                       .message("Itinerary deleted")
                       .statusCode(HttpStatus.OK.value())
                       .data(data)
                       .build());
    }
}
