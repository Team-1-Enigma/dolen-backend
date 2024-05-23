package com.enigma.dolen.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
@Table(name = "t_order_details")
@Getter
@Setter
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String Id;

    @Column(name = "participant_name")
    private String participantName;

    @Column(name = "contact")
    private String contact;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
