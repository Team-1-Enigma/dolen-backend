package com.enigma.dolen.controller;

import com.enigma.dolen.model.dto.AddPhotoRequest;
import com.enigma.dolen.model.dto.CommonResponse;
import com.enigma.dolen.model.dto.ImageTravelResponse;
import com.enigma.dolen.service.ImageTravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/travel/image")
public class ImageTravelController {

    private final ImageTravelService imageTravelService;

    @PostMapping("/{travel_id}")
    public ResponseEntity<?> addImageTravel(@ModelAttribute AddPhotoRequest request){
        List<ImageTravelResponse> imageTravelResponses = imageTravelService.addPhoto(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .message("Image travel added successfully")
                        .statusCode(HttpStatus.CREATED.value())
                        .data(imageTravelResponses)
                        .build());
    }

    @GetMapping("/{travel_id}")
    public ResponseEntity<?> getImageByTravelId(@PathVariable String travel_id){
        List<ImageTravelResponse> imageUrls = imageTravelService.getAllPhotoByTravelId(travel_id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("All image by travel id found successfully")
                        .statusCode(HttpStatus.OK.value())
                        .data(imageUrls)
                        .build());
    }

    @DeleteMapping("/{image_id}")
    public ResponseEntity<?> deleteImageTravel(@PathVariable String image_id){
        imageTravelService.deleteImageTravel(image_id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("Image travel deleted")
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }
}
