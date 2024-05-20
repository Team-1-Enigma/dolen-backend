package com.enigma.dolen.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "m_user_verifications")
public class UserVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "credential_id", nullable = false)
    private UserCredential userCredential;

    @Column(name = "verification_code", nullable = false)
    private int verificationCode;

    @Column(name = "is_verified", nullable = false)
    private Boolean isVerified;
}
