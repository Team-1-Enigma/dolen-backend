package com.enigma.dolen.service.impl;

import com.enigma.dolen.model.dto.*;
import com.enigma.dolen.model.entity.*;
import com.enigma.dolen.repository.FeedbackRepository;
import com.enigma.dolen.repository.UserRepository;
import com.enigma.dolen.service.FeedbackService;
import com.enigma.dolen.service.TravelService;
import com.enigma.dolen.service.UserService;
import jakarta.persistence.EntityNotFoundException;
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
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public FeedbackResponse createFeedback(FeedbackDTO feedbackDTO) {

        // Fetch the travel entity
        Travel travel = travelService.getTravelByIdForOther(feedbackDTO.getTravelId());

        // Fetch the user entity from the database using the user ID
        UserDTO user = userService.getUserById(feedbackDTO.getUserId());
        if (user == null) {
            throw new EntityNotFoundException("User not found with ID: " + feedbackDTO.getUserId());
        }

        // Create the feedback entity
        Feedback feedback = Feedback.builder()
                .travel(travel)
                .feedbackText(feedbackDTO.getFeedbackText())
                .feedbackDate(LocalDate.parse(feedbackDTO.getFeedbackDate()))
                .user(User.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .phoneNumber(user.getPhoneNumber())
                        .build())  // Use the persisted user entity
                .createdAt(LocalDateTime.now())
                .build();

        // Save the feedback entity
        feedbackRepository.save(feedback);

        // Convert the feedback entity to a response DTO
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
