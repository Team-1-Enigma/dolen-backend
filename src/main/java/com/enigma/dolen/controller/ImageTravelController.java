package com.enigma.dolen.controller;

import com.enigma.dolen.model.dto.CommonResponse;
import com.enigma.dolen.model.dto.ImageTravelDTO;
import com.enigma.dolen.model.dto.ImageTravelResponse;
import com.enigma.dolen.service.ImageTravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/travel/image")
public class ImageTravelController {

    private final ImageTravelService imageTravelService;

    @PostMapping
    public ResponseEntity<?> createImageTravel(@RequestBody ImageTravelDTO imageTravelDTO){
        ImageTravelResponse imageTravelResponse = imageTravelService.addPhoto(imageTravelDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .message("Image travel created")
                        .statusCode(HttpStatus.CREATED.value())
                        .data(imageTravelResponse)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImageTravelById(@PathVariable String id){
        ImageTravelResponse imageTravelResponse = imageTravelService.getImageTravelById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("Image travel found")
                        .statusCode(HttpStatus.OK.value())
                        .data(imageTravelResponse)
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImageTravel(@PathVariable String id){
        imageTravelService.deleteImageTravel(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("Image travel deleted")
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }
}
