package com.enigma.dolen.service;

import com.enigma.dolen.model.dto.FeedbackDTO;
import com.enigma.dolen.model.dto.FeedbackResponse;
import com.enigma.dolen.model.entity.Feedback;

import java.util.List;

public interface FeedbackService {
    FeedbackResponse createFeedback(FeedbackDTO feedbackDTO);
    FeedbackResponse updateFeedback(FeedbackDTO feedbackDTO);
    List<FeedbackResponse> getFeedbackByTravelId(String travelId);
}
