package com.aiu.trips.service.impl;

import com.aiu.trips.decorator.*;
import com.aiu.trips.dto.*;
import com.aiu.trips.enums.BookingStatus;
import com.aiu.trips.model.Booking;
import com.aiu.trips.model.Event;
import com.aiu.trips.repository.*;
import com.aiu.trips.service.BookingService;
import com.aiu.trips.service.interfaces.IBookingTicketingSystem;
import com.aiu.trips.strategy.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingTicketingSystemImpl implements IBookingTicketingSystem {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ITicketService ticketService;

    @Autowired
    @Qualifier("defaultPricingStrategy")
    private PricingStrategy pricingStrategy;

    @Autowired
    private BookingService bookingService;

    @Override
    public List<ActivityDTO> browseEvents(EventFilterDTO filters) {
        return eventRepository.findAll().stream()
                .map(this::convertToActivityDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingDTO bookEvent(Long studentId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (event.getAvailableSeats() <= 0) {
            throw new RuntimeException("No seats available");
        }

        Booking booking = new Booking();
        booking.setUser(userRepository.findById(studentId).orElseThrow());
        booking.setEvent(event);
        booking.setBookingDate(LocalDateTime.now());
        booking.setStatus(BookingStatus.CONFIRMED);

        // Use Strategy Pattern for pricing
        BigDecimal price = pricingStrategy.calculatePrice(
                BigDecimal.valueOf(event.getPrice()),
                LocalDateTime.now(),
                1);

        event.setAvailableSeats(event.getAvailableSeats() - 1);
        eventRepository.save(event);

        booking = bookingRepository.save(booking);

        return convertToBookingDTO(booking);
    }

    @Override
    @Transactional
    public BookingDTO bookEventByEmail(Long eventId, String userEmail) {
        Booking booking = bookingService.createBooking(eventId, userEmail);
        return convertToBookingDTO(booking);
    }

    @Override
    public TicketDTO generateTicket(Long bookingId) {
        // Use Decorator Pattern for ticket service
        return ticketService.generateTicket(bookingId);
    }

    @Override
    public boolean validateTicket(String qrCode) {
        // Use Decorator Pattern for ticket validation
        return ticketService.validateQRCode(qrCode);
    }

    private ActivityDTO convertToActivityDTO(Event event) {
        ActivityDTO dto = new ActivityDTO();
        dto.setActivityId(event.getId());
        dto.setName(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setLocation(event.getLocation());
        dto.setPrice(BigDecimal.valueOf(event.getPrice()));
        dto.setCapacity(event.getCapacity());
        dto.setAvailableSeats(event.getAvailableSeats());
        return dto;
    }

    private BookingDTO convertToBookingDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setId(booking.getId());
        dto.setUserId(booking.getUser().getId());
        dto.setEventId(booking.getEvent().getId());
        dto.setStatus(booking.getStatus());
        dto.setBookingDate(booking.getBookingDate());
        return dto;
    }

    @Override
    public List<Booking> getUserBookings(String userEmail) {
        return bookingService.getUserBookings(userEmail);
    }
}
