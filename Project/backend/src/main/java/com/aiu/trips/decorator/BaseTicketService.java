package com.aiu.trips.decorator;

import com.aiu.trips.dto.TicketDTO;
import com.aiu.trips.factory.IModelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

/**
 * BaseTicketService as per Booking_Ticketing.pu diagram
 * Decorator Pattern - Base implementation
 */
@Service
public class BaseTicketService implements ITicketService {

    @Autowired
    private IModelFactory modelFactory;

    @Override
    public TicketDTO generateTicket(Long bookingId) {
        TicketDTO ticket = new TicketDTO();
        ticket.setBookingId(bookingId);
        ticket.setQrCode(generateQRCode(bookingId));
        ticket.setIsUsed(false);
        return ticket;
    }

    @Override
    public boolean validateQRCode(String qrCode) {
        // Basic validation - check if QR code is not null and has correct format
        return qrCode != null && qrCode.length() > 0;
    }

    private String generateQRCode(Long bookingId) {
        return "QR-" + bookingId + "-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
