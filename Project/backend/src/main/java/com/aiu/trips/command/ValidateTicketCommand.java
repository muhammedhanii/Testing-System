package com.aiu.trips.command;

import com.aiu.trips.service.interfaces.IBookingTicketingSystem;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public class ValidateTicketCommand implements IControllerCommand {
    private final IBookingTicketingSystem bookingService;

    public ValidateTicketCommand(IBookingTicketingSystem bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    public ResponseEntity<?> execute(Map<String, Object> requestData) {
        try {
            String qrCode = (String) requestData.get("qrCode");
            boolean isValid = bookingService.validateTicket(qrCode);
            return ResponseEntity.ok(Map.of("valid", isValid));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
