package com.enigma.dolen.controller;

import com.enigma.dolen.model.dto.AddPhotoTripRequest;
import com.enigma.dolen.model.dto.CommonResponse;
import com.enigma.dolen.model.dto.ImageTripResponse;
import com.enigma.dolen.service.ImageTripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/travel/trips/image")
public class ImageTripController {

    private final ImageTripService imageTripService;

    @PostMapping("/{trip_id}")
    public ResponseEntity<?> addImageTrip(@ModelAttribute AddPhotoTripRequest request){
        List<ImageTripResponse> imageTripResponses = imageTripService.addPhoto(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .message("Image trip added successfully")
                        .statusCode(HttpStatus.CREATED.value())
                        .data(imageTripResponses)
                        .build());
    }

    @GetMapping("/{trip_id}")
    public ResponseEntity<?> getImageByTravelId(@PathVariable String trip_id){
        List<ImageTripResponse> imageTripResponses = imageTripService.getAllPhotoByTravelId(trip_id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("All image by trip id found successfully")
                        .statusCode(HttpStatus.OK.value())
                        .data(imageTripResponses)
                        .build());
    }

    @DeleteMapping("/{image_id}")
    public ResponseEntity<?> deleteImageTrip(@PathVariable String image_id){
        imageTripService.deleteImageTrip(image_id);

        return ResponseEntity.status(HttpStatus.OK)
               .body(CommonResponse.builder()
                       .message("Image trip deleted")
                       .statusCode(HttpStatus.OK.value())
                       .build());
    }
}
