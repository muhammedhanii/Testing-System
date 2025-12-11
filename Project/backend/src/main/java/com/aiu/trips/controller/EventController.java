package com.aiu.trips.controller;

import com.aiu.trips.chain.RequestHandler;
import com.aiu.trips.command.*;
import com.aiu.trips.service.interfaces.IActivityManagement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * EventController - Uses Command Pattern and Chain of Responsibility for all
 * operations
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private ControllerCommandInvoker commandInvoker;

    @Autowired
    private IActivityManagement activityService;

    @Autowired
    @Qualifier("requestHandlerChain")
    private RequestHandler handlerChain;

    @GetMapping
    public ResponseEntity<?> getAllEvents(HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            IControllerCommand command = new GetAllActivitiesCommand(activityService);
            commandInvoker.pushToQueue(command);
            return commandInvoker.executeNext(new HashMap<>());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/upcoming")
    public ResponseEntity<?> getUpcomingEvents(HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            IControllerCommand command = new GetUpcomingEventsCommand(activityService);
            commandInvoker.pushToQueue(command);
            return commandInvoker.executeNext(new HashMap<>());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable Long id, HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            // Using GetAllActivitiesCommand and filtering (simplified)
            return getAllEvents(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody Map<String, Object> eventData, HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            IControllerCommand command = new CreateEventCommand(activityService);
            commandInvoker.pushToQueue(command);
            return commandInvoker.executeNext(eventData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody Map<String, Object> eventData,
            HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            eventData.put("id", id);
            IControllerCommand command = new UpdateEventCommand(activityService);
            commandInvoker.pushToQueue(command);
            return commandInvoker.executeNext(eventData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id, HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            Map<String, Object> data = new HashMap<>();
            data.put("id", id);
            IControllerCommand command = new DeleteEventCommand(activityService);
            commandInvoker.pushToQueue(command);
            return commandInvoker.executeNext(data);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
