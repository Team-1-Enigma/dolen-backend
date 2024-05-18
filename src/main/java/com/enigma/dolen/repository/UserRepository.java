package com.enigma.dolen.repository;

import com.enigma.dolen.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
