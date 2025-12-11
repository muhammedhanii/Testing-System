package com.aiu.trips.decorator;

import com.aiu.trips.dto.TicketDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AuditLogDecorator as per Booking_Ticketing.pu diagram
 * Decorator Pattern - Adds audit logging to ticket operations
 */
public class AuditLogDecorator extends TicketServiceDecorator {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogDecorator.class);

    public AuditLogDecorator(ITicketService service) {
        super(service);
    }

    @Override
    public TicketDTO generateTicket(Long bookingId) {
        logger.info("AUDIT: Generating ticket for booking ID: {}", bookingId);
        TicketDTO ticket = super.generateTicket(bookingId);
        logger.info("AUDIT: Ticket generated successfully with QR: {}", ticket.getQrCode());
        return ticket;
    }

    @Override
    public boolean validateQRCode(String qrCode) {
        logger.info("AUDIT: Validating QR code: {}", qrCode);
        boolean isValid = super.validateQRCode(qrCode);
        logger.info("AUDIT: QR code validation result: {}", isValid ? "VALID" : "INVALID");
        return isValid;
    }
}
