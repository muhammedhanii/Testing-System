package com.aiu.trips.command;

import com.aiu.trips.command.IControllerCommand;
import com.aiu.trips.service.interfaces.INotificationSystem;
import org.springframework.http.ResponseEntity;
import java.util.Map;

/**
 * GetUnreadNotificationsCommand - Implements Command Pattern for retrieving
 * unread notifications
 */
public class GetUnreadNotificationsCommand implements IControllerCommand {

    private final INotificationSystem notificationService;

    public GetUnreadNotificationsCommand(INotificationSystem notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public ResponseEntity<?> execute(Map<String, Object> data) {
        try {
            String userEmail = (String) data.get("userEmail");
            return ResponseEntity.ok(notificationService.getUnreadNotifications(userEmail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
