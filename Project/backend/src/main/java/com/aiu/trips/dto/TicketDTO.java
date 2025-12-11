package com.aiu.trips.dto;

import java.time.LocalDateTime;

/**
 * TicketDTO for transferring ticket data
 */
public class TicketDTO {
    private Long ticketId;
    private Long bookingId;
    private String qrCode;
    private Boolean isUsed;
    private LocalDateTime issueDate;

    public TicketDTO() {}

    // Getters and Setters
    public Long getTicketId() { return ticketId; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }

    public Boolean getIsUsed() { return isUsed; }
    public void setIsUsed(Boolean isUsed) { this.isUsed = isUsed; }

    public LocalDateTime getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDateTime issueDate) { this.issueDate = issueDate; }
}
