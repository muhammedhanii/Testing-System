package com.aiu.trips.memento;

import com.aiu.trips.enums.BookingStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * BookingMemento entity as per Data_Layer.pu diagram
 * Stores snapshot of Booking state
 */
@Entity
@Table(name = "booking_mementos")
public class BookingMemento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long bookingId;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private Long activityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(nullable = false)
    private LocalDateTime savedAt;

    @PrePersist
    protected void onCreate() {
        savedAt = LocalDateTime.now();
    }

    // Constructors
    public BookingMemento() {}

    public BookingMemento(Long bookingId, Long studentId, Long activityId, BookingStatus status) {
        this.bookingId = bookingId;
        this.studentId = studentId;
        this.activityId = activityId;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(LocalDateTime savedAt) {
        this.savedAt = savedAt;
    }
}
