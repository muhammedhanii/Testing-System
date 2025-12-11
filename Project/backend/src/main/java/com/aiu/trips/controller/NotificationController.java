package com.aiu.trips.controller;

import com.aiu.trips.chain.RequestHandler;
import com.aiu.trips.command.*;
import com.aiu.trips.service.interfaces.INotificationSystem;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * NotificationController - Uses Command Pattern and Chain of Responsibility for
 * all operations
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private ControllerCommandInvoker commandInvoker;

    @Autowired
    private INotificationSystem notificationService;

    @Autowired
    @Qualifier("requestHandlerChain")
    private RequestHandler handlerChain;

    @GetMapping
    public ResponseEntity<?> getNotifications(HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            // Extract user email from authentication
            String userEmail = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null;

            Map<String, Object> data = new HashMap<>();
            data.put("userEmail", userEmail);

            IControllerCommand command = new GetNotificationsCommand(notificationService);
            commandInvoker.pushToQueue(command);
            return commandInvoker.executeNext(data);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/unread")
    public ResponseEntity<?> getUnreadNotifications(HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            // Extract user email from authentication
            String userEmail = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : null;

            Map<String, Object> data = new HashMap<>();
            data.put("userEmail", userEmail);

            IControllerCommand command = new GetUnreadNotificationsCommand(notificationService);
            commandInvoker.pushToQueue(command);
            return commandInvoker.executeNext(data);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendNotification(@RequestBody Map<String, Object> requestData,
            HttpServletRequest request) {
        try {
            handlerChain.handle(request);
            IControllerCommand command = new SendNotificationCommand(notificationService);
            commandInvoker.pushToQueue(command);
            return commandInvoker.executeNext(requestData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
