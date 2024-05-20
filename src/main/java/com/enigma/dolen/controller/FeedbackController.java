package com.enigma.dolen.controller;

import com.enigma.dolen.model.dto.CommonResponse;
import com.enigma.dolen.model.dto.FeedbackDTO;
import com.enigma.dolen.model.dto.FeedbackResponse;
import com.enigma.dolen.model.entity.Feedback;
import com.enigma.dolen.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/travel/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<?> createFeedback(@RequestBody FeedbackDTO feedbackDTO){
        FeedbackResponse feedbackResponse = feedbackService.createFeedback(feedbackDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .message("Feedback created")
                        .statusCode(HttpStatus.CREATED.value())
                        .data(feedbackResponse)
                        .build());
    }

    @PutMapping
    public ResponseEntity<?> updateFeedback(@RequestBody FeedbackDTO feedbackDTO){
        FeedbackResponse feedbackResponse = feedbackService.updateFeedback(feedbackDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("Feedback updated")
                        .statusCode(HttpStatus.OK.value())
                        .data(feedbackResponse)
                        .build());
    }

    @GetMapping("/{travelId}")
    public ResponseEntity<?> getFeedbackByTravelId(@PathVariable String travelId){
        List<FeedbackResponse> feedbackResponses = feedbackService.getFeedbackByTravelId(travelId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .message("get all feedback by travel id")
                        .statusCode(HttpStatus.OK.value())
                        .data(feedbackResponses)
                        .build());
    }
}
