package com.enigma.dolen.repository;

import com.enigma.dolen.model.entity.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVerificationRepository extends JpaRepository<UserVerification, String>{

    @Query("SELECT uv FROM UserVerification uv WHERE uv.verificationCode = ?1")
    public UserVerification findByVerificationCode(int verificationCode);
}
