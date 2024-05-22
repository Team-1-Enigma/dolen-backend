package com.enigma.dolen.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipantDTO {
    private String id;
    private String tripId;
    private String participantName;
    private String contact;

}
