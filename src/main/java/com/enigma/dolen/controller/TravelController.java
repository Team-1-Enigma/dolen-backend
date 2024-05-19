package com.enigma.dolen.controller;

import com.enigma.dolen.model.dto.CommonResponse;
import com.enigma.dolen.model.dto.TravelDTO;
import com.enigma.dolen.model.dto.TravelResponse;
import com.enigma.dolen.model.entity.Travel;
import com.enigma.dolen.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/travel")
public class TravelController {

    private final TravelService travelService;

    @PostMapping
    public ResponseEntity<?> createTravel(@RequestBody TravelDTO travelDTO){
        TravelResponse travelResponse = travelService.createTravel(travelDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .message("Travel created")
                        .statusCode(HttpStatus.CREATED.value())
                        .data(travelResponse)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTravelById(@PathVariable String id){
        TravelResponse travelResponse = travelService.getTravelById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("Travel found")
                        .statusCode(HttpStatus.OK.value())
                        .data(travelResponse)
                        .build());
    }

    @GetMapping
    public ResponseEntity<?> getAllTravel(){
        List<TravelResponse> travelResponses = travelService.getAllTravel();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("Successfully get all travel")
                        .statusCode(HttpStatus.OK.value())
                        .data(travelResponses)
                        .build());
    }

    @PutMapping
    public ResponseEntity<?> updateTravel(@RequestBody TravelDTO travelDTO){
        TravelResponse travelResponse = travelService.updateTravel(travelDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("Travel updated")
                        .statusCode(HttpStatus.OK.value())
                        .data(travelResponse)
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTravel(@PathVariable String id){
        travelService.deleteTravel(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("Travel deleted")
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }
}
