package com.enigma.dolen.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "m_trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "travel_id", nullable = false)
    private Travel travel;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

    @OneToMany(mappedBy = "trip")
    private List<TripPrice> tripPrices;

    @OneToMany(mappedBy = "trip")
    private List<ImageTrip> imageTrips;

    @OneToMany(mappedBy = "trip")
    private List<Participant> participants;

    @OneToMany(mappedBy = "trip")
    private List<Itinerary> itineraries;
}
