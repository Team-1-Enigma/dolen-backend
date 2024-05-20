package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.dto.BankAccountResponse;
import com.enigma.dolen.model.dto.FeedbackDTO;
import com.enigma.dolen.model.dto.FeedbackResponse;
import com.enigma.dolen.model.dto.TravelDTO;
import com.enigma.dolen.model.entity.BankAccount;
import com.enigma.dolen.model.entity.Feedback;
import com.enigma.dolen.model.entity.Travel;
import com.enigma.dolen.repository.FeedbackRepository;
import com.enigma.dolen.service.FeedbackService;
import com.enigma.dolen.service.TravelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final TravelService travelService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public FeedbackResponse createFeedback(FeedbackDTO feedbackDTO) {

        Travel travel = travelService.getTravelByIdForOther(feedbackDTO.getTravelId());

        Feedback feedback = Feedback.builder()
                .travel(travel)
                .feedbackText(feedbackDTO.getFeedbackText())
                .feedbackDate(LocalDate.parse(feedbackDTO.getFeedbackDate()))
                .createdAt(LocalDateTime.now())
                .build();
        feedbackRepository.saveAndFlush(feedback);

        return toFeedbackResponse(feedback);
    }

    private static FeedbackResponse toFeedbackResponse(Feedback feedback) {
        FeedbackResponse feedbackResponse = FeedbackResponse.builder()
                .id(feedback.getId())
                .travelDTO(TravelDTO.builder()
                        .id(feedback.getTravel().getId())
                        .userId(feedback.getTravel().getUser().getId())
                        .name(feedback.getTravel().getName())
                        .contactInfo(feedback.getTravel().getContactInfo())
                        .address(feedback.getTravel().getAddress())
                        .build())
                .feedbackText(feedback.getFeedbackText())
                .feedbackDate(String.valueOf(feedback.getFeedbackDate()))
                .build();
        return feedbackResponse;
    }

    @Override
    public FeedbackResponse updateFeedback(FeedbackDTO feedbackDTO) {
        Feedback existingFeedback = feedbackRepository.findById(feedbackDTO.getId()).orElse(null);
        if (existingFeedback == null){
            return null;
        }
        Feedback saveFeedback = Feedback.builder()
                .id(existingFeedback.getId())
                .travel(existingFeedback.getTravel())
                .feedbackText(feedbackDTO.getFeedbackText())
                .feedbackDate(LocalDate.parse(feedbackDTO.getFeedbackDate()))
                .updatedAt(LocalDateTime.now())
                .build();
        feedbackRepository.saveAndFlush(saveFeedback);

        return toFeedbackResponse(saveFeedback);
    }

    @Override
    public List<FeedbackResponse> getFeedbackByTravelId(String travelId) {
        List<Feedback> feedbacks = feedbackRepository.getFeedbackByTravelId(travelId);
        return feedbacks.stream()
                .map(feedback -> toFeedbackResponse(feedback))
                .collect(Collectors.toList());
    }
}
