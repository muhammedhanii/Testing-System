package com.aiu.trips.service.interfaces;

import com.aiu.trips.dto.BookingDTO;
import com.aiu.trips.dto.EventFilterDTO;
import com.aiu.trips.dto.ActivityDTO;
import com.aiu.trips.dto.TicketDTO;
import com.aiu.trips.model.Booking;
import java.util.List;

/**
 * IBookingTicketingSystem interface as per Controller.pu diagram
 */
public interface IBookingTicketingSystem {
    List<ActivityDTO> browseEvents(EventFilterDTO filters);

    BookingDTO bookEvent(Long studentId, Long eventId);

    BookingDTO bookEventByEmail(Long eventId, String userEmail);

    TicketDTO generateTicket(Long bookingId);

    boolean validateTicket(String qrCode);

    // Additional methods for booking operations
    List<Booking> getUserBookings(String userEmail);
}
