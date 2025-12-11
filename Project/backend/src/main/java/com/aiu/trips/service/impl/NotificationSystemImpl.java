package com.aiu.trips.service.impl;

import com.aiu.trips.bridge.*;
import com.aiu.trips.enums.NotificationType;
import com.aiu.trips.model.Notification;
import com.aiu.trips.model.User;
import com.aiu.trips.repository.UserRepository;
import com.aiu.trips.service.NotificationService;
import com.aiu.trips.service.interfaces.INotificationSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationSystemImpl implements INotificationSystem {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailChannel emailChannel;

    @Autowired
    private InAppChannel inAppChannel;

    @Autowired
    private NotificationService notificationService;

    @Override
    public void sendNotification(Long userId, String message, NotificationType type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Use Bridge Pattern
        NotificationChannel channel = (type == NotificationType.NEW_EVENT) ? emailChannel : inAppChannel;
        NotificationMessage notificationMessage = createMessage(channel, type, message);
        notificationMessage.send(user.getEmail());
    }

    @Override
    public void notifyEventUpdate(Long eventId, String message) {
        // Use Bridge Pattern
        EventUpdateMessage updateMessage = new EventUpdateMessage(emailChannel, "Event", message);
        // Notify all users (simplified)
        List<User> users = userRepository.findAll();
        users.forEach(user -> updateMessage.send(user.getEmail()));
    }

    @Override
    public void sendBulkNotification(List<Long> userIds, String message) {
        userIds.forEach(userId -> {
            User user = userRepository.findById(userId).orElse(null);
            if (user != null) {
                inAppChannel.send(user.getEmail(), message);
            }
        });
    }

    @Override
    public List<Notification> getUserNotifications(String userEmail) {
        return notificationService.getUserNotifications(userEmail);
    }

    @Override
    public List<Notification> getUnreadNotifications(String userEmail) {
        return notificationService.getUnreadNotifications(userEmail);
    }

    @Override
    public void markAsRead(Long notificationId) {
        notificationService.markAsRead(notificationId);
    }

    private NotificationMessage createMessage(NotificationChannel channel, NotificationType type, String message) {
        switch (type) {
            case NEW_EVENT:
                return new NewEventMessage(channel, "New Event", LocalDateTime.now());
            case EVENT_UPDATE:
                return new EventUpdateMessage(channel, "Event", message);
            case REMINDER:
                return new ReminderMessage(channel, "Event", 24);
            default:
                return new EventUpdateMessage(channel, "Notification", message);
        }
    }
}
