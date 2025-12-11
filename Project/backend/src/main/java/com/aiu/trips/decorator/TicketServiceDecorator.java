package com.aiu.trips.decorator;

import com.aiu.trips.dto.TicketDTO;

/**
 * TicketServiceDecorator as per Booking_Ticketing.pu diagram
 * Decorator Pattern - Abstract decorator
 */
public abstract class TicketServiceDecorator implements ITicketService {

    protected ITicketService wrappedService;

    public TicketServiceDecorator(ITicketService service) {
        this.wrappedService = service;
    }

    @Override
    public TicketDTO generateTicket(Long bookingId) {
        return wrappedService.generateTicket(bookingId);
    }

    @Override
    public boolean validateQRCode(String qrCode) {
        return wrappedService.validateQRCode(qrCode);
    }
}
