package com.aiu.trips.repository;

import com.aiu.trips.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByEvent_Id(Long eventId);
    List<Feedback> findByUser_Id(Long userId);
    boolean existsByUser_IdAndEvent_Id(Long userId, Long eventId);
}
