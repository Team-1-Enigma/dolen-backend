package com.enigma.dolen.controller;

import com.enigma.dolen.model.dto.CommonResponse;
import com.enigma.dolen.model.dto.TripPriceDiscountResponse;
import com.enigma.dolen.model.dto.TripPriceRequest;
import com.enigma.dolen.model.dto.TripPriceResponse;
import com.enigma.dolen.service.TripPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/travel/trip")
public class TripPriceController {

    private final TripPriceService tripPriceService;

    @PostMapping("/{id}/price")
    public ResponseEntity<?> createTripPrice(@PathVariable String id, @RequestBody TripPriceRequest request){
        TripPriceResponse response = tripPriceService.addTripPrice(id, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .message("Trip price created successfully")
                        .statusCode(HttpStatus.CREATED.value())
                        .data(response)
                        .build());
    }

    @GetMapping("/{id}/price")
    public ResponseEntity<?> getTripPriceByTripId(@PathVariable String id){
        List<TripPriceResponse> tripPriceResponses = tripPriceService.getTripPriceByTripId(id);

        return ResponseEntity.status(HttpStatus.OK)
               .body(CommonResponse.builder()
                       .message("Trip price found")
                       .statusCode(HttpStatus.OK.value())
                       .data(tripPriceResponses)
                       .build());
    }

    @GetMapping("/{id}/price/discount")
    public ResponseEntity<?> getTripPriceDiscount(@PathVariable String id){
        TripPriceDiscountResponse response = tripPriceService.getTripPriceDiscount(id);

        return ResponseEntity.status(HttpStatus.OK)
               .body(CommonResponse.builder()
                       .message("Trip price discount found")
                       .statusCode(HttpStatus.OK.value())
                       .data(response)
                       .build());
    }
}
