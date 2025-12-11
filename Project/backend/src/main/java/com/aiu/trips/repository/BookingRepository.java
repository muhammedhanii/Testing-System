package com.aiu.trips.repository;

import com.aiu.trips.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser_Id(Long userId);
    List<Booking> findByEvent_Id(Long eventId);
    List<Booking> findByUser_IdAndEvent_Id(Long userId, Long eventId);
    Optional<Booking> findByBookingCode(String bookingCode);
    boolean existsByUser_IdAndEvent_Id(Long userId, Long eventId);
}
