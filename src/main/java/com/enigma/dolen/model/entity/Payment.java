package com.enigma.dolen.model.entity;

import com.enigma.dolen.constant.EPaymentStatus;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "m_payment")
@Getter
@Setter
@Builder(toBuilder = true)
public class Payment {
    @Id
    private String Id;

    @Column(name = "payment_link")
    private String paymentLink;

    @Column(name = "total")
    private Long total;

    @Column(name = "status")
    private EPaymentStatus paymentStatus;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
