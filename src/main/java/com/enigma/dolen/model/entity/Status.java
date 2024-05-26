package com.enigma.dolen.model.entity;

import com.enigma.dolen.constant.EStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import jakarta.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder(toBuilder = true)
@Getter
@Setter
@Table(name = "m_order_status")
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String Id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EStatus  status;

    @Column(name = "notes")
    private String notes;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
