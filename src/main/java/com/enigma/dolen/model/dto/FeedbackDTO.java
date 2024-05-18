package com.enigma.dolen.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackDTO {
    private String id;
    private String feedbackText;
    private String feedbackDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String travelId;
}
