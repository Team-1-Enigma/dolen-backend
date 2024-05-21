package com.enigma.dolen.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "m_itinerary_details")
public class ItineraryDetail {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private String id;

        @ManyToOne
        @JoinColumn(name = "itinerary_id", nullable = false)
        private Itinerary itinerary;

        @Column(name = "start_time", nullable = false)
        private LocalDateTime startTime;

        @Column(name = "end_time", nullable = false)
        private LocalDateTime endTime;

        @Column(name = "activity_desc", nullable = false)
        private String activityDesc;
}
