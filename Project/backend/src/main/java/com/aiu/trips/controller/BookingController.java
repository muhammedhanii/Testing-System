package com.aiu.trips.controller;

import com.aiu.trips.chain.RequestHandler;
import com.aiu.trips.command.*;
import com.aiu.trips.service.interfaces.IBookingTicketingSystem;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * BookingController - Uses Command Pattern and Chain of Responsibility for all
 * operations
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private ControllerCommandInvoker commandInvoker;

    @Autowired
    private IBookingTicketingSystem bookingService;

    @Autowired
    @Qualifier("requestHandlerChain")
    private RequestHandler handlerChain;

    @GetMapping("/browse")
    public ResponseEntity<?> browseEvents(HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            IControllerCommand command = new BrowseEventsCommand(bookingService);
            commandInvoker.pushToQueue(command);
            return commandInvoker.executeNext(new HashMap<>());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<?> getMyBookings(HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            // Extract user email from authentication
            String userEmail = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null;

            Map<String, Object> data = new HashMap<>();
            data.put("userEmail", userEmail);

            IControllerCommand command = new GetUserBookingsCommand(bookingService);
            commandInvoker.pushToQueue(command);
            return commandInvoker.executeNext(data);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/event/{eventId}")
    public ResponseEntity<?> createBooking(@PathVariable Long eventId, HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            // Extract user email from authentication
            String userEmail = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "guest";

            Map<String, Object> data = new HashMap<>();
            data.put("eventId", eventId);
            data.put("userEmail", userEmail);

            IControllerCommand command = new BookEventCommand(bookingService);
            commandInvoker.pushToQueue(command);
            return commandInvoker.executeNext(data);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateTicket(@RequestBody Map<String, Object> requestData, HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            IControllerCommand command = new ValidateTicketCommand(bookingService);
            commandInvoker.pushToQueue(command);
            return commandInvoker.executeNext(requestData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
