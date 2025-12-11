package com.aiu.trips.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Ticket entity as per Data_Layer.pu diagram
 */
@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @Column(nullable = false)
    private Long bookingId;

    @Column(nullable = false, unique = true)
    private String qrCode;

    @Column(nullable = false)
    private Boolean isUsed = false;

    @Column(nullable = false)
    private LocalDateTime issueDate;

    @PrePersist
    protected void onCreate() {
        issueDate = LocalDateTime.now();
        if (isUsed == null) {
            isUsed = false;
        }
    }

    // Constructors
    public Ticket() {}

    public Ticket(Long bookingId, String qrCode) {
        this.bookingId = bookingId;
        this.qrCode = qrCode;
        this.isUsed = false;
    }

    // Getters and Setters
    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }
}
