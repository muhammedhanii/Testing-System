package com.aiu.trips.command;

import com.aiu.trips.dto.EventFilterDTO;
import com.aiu.trips.service.interfaces.IBookingTicketingSystem;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public class BrowseEventsCommand implements IControllerCommand {
    private final IBookingTicketingSystem bookingService;

    public BrowseEventsCommand(IBookingTicketingSystem bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    public ResponseEntity<?> execute(Map<String, Object> requestData) {
        try {
            EventFilterDTO filters = new EventFilterDTO();
            return ResponseEntity.ok(bookingService.browseEvents(filters));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
