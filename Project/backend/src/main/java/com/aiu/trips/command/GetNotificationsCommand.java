package com.aiu.trips.command;

import com.aiu.trips.service.interfaces.INotificationSystem;
import com.aiu.trips.command.IControllerCommand;
import org.springframework.http.ResponseEntity;
import java.util.Map;

/**
 * GetNotificationsCommand - Implements Command Pattern for retrieving user
 * notifications
 */
public class GetNotificationsCommand implements IControllerCommand {

    private final INotificationSystem notificationService;

    public GetNotificationsCommand(INotificationSystem notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public ResponseEntity<?> execute(Map<String, Object> data) {
        try {
            String userEmail = (String) data.get("userEmail");
            return ResponseEntity.ok(notificationService.getUserNotifications(userEmail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
