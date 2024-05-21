package com.enigma.dolen.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "m_trip_prices")
public class TripPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "quota", nullable = false)
    private Integer quota;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}
