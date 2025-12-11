package com.aiu.trips.command;

import com.aiu.trips.command.IControllerCommand;
import com.aiu.trips.service.interfaces.IBookingTicketingSystem;
import org.springframework.http.ResponseEntity;
import java.util.Map;

/**
 * GetUserBookingsCommand - Implements Command Pattern for retrieving user's
 * bookings
 */
public class GetUserBookingsCommand implements IControllerCommand {

    private final IBookingTicketingSystem bookingService;

    public GetUserBookingsCommand(IBookingTicketingSystem bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    public ResponseEntity<?> execute(Map<String, Object> data) {
        try {
            String userEmail = (String) data.get("userEmail");
            return ResponseEntity.ok(bookingService.getUserBookings(userEmail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
