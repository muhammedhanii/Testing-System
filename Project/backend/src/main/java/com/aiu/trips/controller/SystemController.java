package com.aiu.trips.controller;

import com.aiu.trips.chain.*;
import com.aiu.trips.command.*;
import com.aiu.trips.service.interfaces.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * SystemController - Uses Command Pattern and Chain of Responsibility
 */
@RestController
@RequestMapping("/api/v2")
public class SystemController {

    @Autowired
    private ControllerCommandInvoker commandInvoker;

    @Autowired
    private IAuthenticationUserManagement authService;

    @Autowired
    private IActivityManagement activityService;

    @Autowired
    private IBookingTicketingSystem bookingService;

    @Autowired
    private INotificationSystem notificationService;

    @Autowired
    @Qualifier("requestHandlerChain")
    private RequestHandler handlerChain;

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> requestData, HttpServletRequest request) {
        try {
            // Use Chain of Responsibility
            handlerChain.handle(request);

            // Use Command Pattern
            IControllerCommand command = new RegisterCommand(authService);
            commandInvoker.pushToQueue(command);
            return commandInvoker.executeNext(requestData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> requestData, HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            IControllerCommand command = new LoginCommand(authService);
            commandInvoker.pushToQueue(command);
            return commandInvoker.executeNext(requestData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/activities")
    public ResponseEntity<?> createActivity(@RequestBody Map<String, Object> requestData, HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            IControllerCommand command = new CreateEventCommand(activityService);
            commandInvoker.pushToQueue(command);
            return commandInvoker.executeNext(requestData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> bookEvent(@RequestBody Map<String, Object> requestData, HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            IControllerCommand command = new BookEventCommand(bookingService);
            commandInvoker.pushToQueue(command);
            return commandInvoker.executeNext(requestData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/activities")
    public ResponseEntity<?> getAllActivities() {
        try {
            return ResponseEntity.ok(activityService.getAllActivities());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
