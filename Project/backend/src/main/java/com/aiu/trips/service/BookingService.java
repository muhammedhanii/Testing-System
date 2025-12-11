package com.aiu.trips.service;

import com.aiu.trips.constants.AppConstants;
import com.aiu.trips.enums.BookingStatus;
import com.aiu.trips.enums.EventStatus;
import com.aiu.trips.exception.BookingException;
import com.aiu.trips.exception.ResourceNotFoundException;
import com.aiu.trips.model.Booking;
import com.aiu.trips.model.Event;
import com.aiu.trips.model.User;
import com.aiu.trips.repository.BookingRepository;
import com.aiu.trips.repository.EventRepository;
import com.aiu.trips.repository.UserRepository;
import com.aiu.trips.util.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QRCodeGenerator qrCodeGenerator;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public Booking createBooking(Long eventId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userEmail));
        
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new ResourceNotFoundException(AppConstants.EVENT_NOT_FOUND + eventId));

        if (event.getAvailableSeats() <= 0) {
            throw new BookingException(AppConstants.NO_SEATS_AVAILABLE);
        }

        if (bookingRepository.existsByUser_IdAndEvent_Id(user.getId(), eventId)) {
            throw new BookingException("Already booked this event");
        }

        // Update available seats
        event.setAvailableSeats(event.getAvailableSeats() - 1);
        eventRepository.save(event);

        // Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setEvent(event);
        booking.setBookingCode(UUID.randomUUID().toString());
        booking.setAmountPaid(event.getPrice());

        // Generate QR code
        try {
            String qrData = "BOOKING:" + booking.getBookingCode() + "|EVENT:" + event.getId();
            String qrCode = qrCodeGenerator.generateQRCodeBase64(qrData);
            booking.setQrCodePath(qrCode);
        } catch (Exception e) {
            throw new BookingException("Failed to generate QR code: " + e.getMessage());
        }

        Booking savedBooking = bookingRepository.save(booking);

        // Send notification
        notificationService.notifyUser(
            user.getId(),
            "Booking confirmed for: " + event.getTitle(),
            "SUCCESS"
        );

        return savedBooking;
    }

    public void cancelBooking(Long bookingId, String userEmail) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new ResourceNotFoundException(AppConstants.BOOKING_NOT_FOUND + bookingId));

        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userEmail));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new BookingException("Unauthorized to cancel this booking");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        // Update available seats
        Event event = booking.getEvent();
        event.setAvailableSeats(event.getAvailableSeats() + 1);
        eventRepository.save(event);

        // Send notification
        notificationService.notifyUser(
            user.getId(),
            "Booking cancelled for: " + event.getTitle(),
            "INFO"
        );
    }

    public List<Booking> getUserBookings(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userEmail));
        return bookingRepository.findByUser_Id(user.getId());
    }

    public List<Booking> getEventBookings(Long eventId) {
        return bookingRepository.findByEvent_Id(eventId);
    }

    public Booking getBookingByCode(String bookingCode) {
        return bookingRepository.findByBookingCode(bookingCode)
            .orElseThrow(() -> new ResourceNotFoundException(AppConstants.BOOKING_NOT_FOUND + bookingCode));
    }

    @Transactional
    public Booking validateTicket(String bookingCode, String validatedByEmail) {
        Booking booking = bookingRepository.findByBookingCode(bookingCode)
            .orElseThrow(() -> new ResourceNotFoundException(AppConstants.BOOKING_NOT_FOUND + bookingCode));

        // Check if already validated
        if (BookingStatus.ATTENDED.equals(booking.getStatus())) {
            throw new BookingException(AppConstants.BOOKING_ALREADY_VALIDATED);
        }

        // Check if booking is cancelled
        if (BookingStatus.CANCELLED.equals(booking.getStatus())) {
            throw new BookingException(AppConstants.CANNOT_VALIDATE_CANCELLED);
        }

        // Check if event is active
        Event event = booking.getEvent();
        if (!EventStatus.ACTIVE.equals(event.getStatus())) {
            throw new BookingException("Event is not active");
        }

        // Validate the ticket
        booking.setStatus(BookingStatus.ATTENDED);
        booking.setValidatedAt(java.time.LocalDateTime.now());
        booking.setValidatedBy(validatedByEmail);

        Booking validatedBooking = bookingRepository.save(booking);

        // Send notification to user
        notificationService.notifyUser(
            booking.getUser().getId(),
            "Your ticket for " + event.getTitle() + " has been validated. Enjoy the event!",
            "SUCCESS"
        );

        return validatedBooking;
    }
}
