package com.aiu.trips.command;

import com.aiu.trips.enums.NotificationType;
import com.aiu.trips.service.interfaces.INotificationSystem;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public class SendNotificationCommand implements IControllerCommand {
    private final INotificationSystem notificationService;

    public SendNotificationCommand(INotificationSystem notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public ResponseEntity<?> execute(Map<String, Object> requestData) {
        try {
            Long userId = Long.valueOf(requestData.get("userId").toString());
            String message = (String) requestData.get("message");
            NotificationType type = NotificationType.valueOf((String) requestData.getOrDefault("type", "EVENT_UPDATE"));
            
            notificationService.sendNotification(userId, message, type);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
