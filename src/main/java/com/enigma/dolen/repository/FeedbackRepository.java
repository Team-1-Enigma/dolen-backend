package com.enigma.dolen.repository;

import com.enigma.dolen.model.dto.FeedbackResponse;
import com.enigma.dolen.model.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, String> {

    @Query(nativeQuery = true, value = "SELECT * FROM t_feedbacks WHERE travel_id = :travelId")
    List<Feedback> getFeedbackByTravelId(@Param("travelId") String travelId);
}
