package com.enigma.dolen.controller;

import com.enigma.dolen.model.dto.CommonResponse;
import com.enigma.dolen.model.dto.ItineraryDetailDTO;
import com.enigma.dolen.service.ItineraryDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/travel/trips/actionDetail")
public class ItineraryDetailController {

    private final ItineraryDetailService itineraryDetailService;

    @PutMapping("/{action_detail_id}")
    public ResponseEntity<?> updateItineraryDetail(@PathVariable String action_detail_id, @RequestBody ItineraryDetailDTO itineraryDetailDTO){
        ItineraryDetailDTO itineraryDetail = itineraryDetailService.update(action_detail_id, itineraryDetailDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("Itinerary Detail updated successfully")
                        .statusCode(HttpStatus.OK.value())
                        .data(itineraryDetail)
                        .build());

    }

    @DeleteMapping("/{action_detail_id}")
    public ResponseEntity<?> deleteItineraryDetail(@PathVariable String action_detail_id){
        itineraryDetailService.delete(action_detail_id);

        return ResponseEntity.status(HttpStatus.OK)
               .body(CommonResponse.builder()
                       .message("Itinerary Detail deleted successfully")
                       .statusCode(HttpStatus.OK.value())
                       .build());
    }
}
