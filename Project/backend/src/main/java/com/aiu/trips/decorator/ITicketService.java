package com.aiu.trips.decorator;

import com.aiu.trips.dto.TicketDTO;

/**
 * ITicketService interface as per Booking_Ticketing.pu diagram
 * Component interface for Decorator Pattern
 */
public interface ITicketService {
    TicketDTO generateTicket(Long bookingId);
    boolean validateQRCode(String qrCode);
}
