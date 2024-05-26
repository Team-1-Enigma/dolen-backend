package com.enigma.dolen.repository;

import com.enigma.dolen.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    Optional<List<Order>> findOrdersByUserId(String userId);
    Optional<List<Order>> findOrderByTripId(String tripId);
}
