package com.aiu.trips.service.interfaces;

import com.aiu.trips.enums.NotificationType;
import com.aiu.trips.model.Notification;
import java.util.List;

/**
 * INotificationSystem interface as per Controller.pu diagram
 */
public interface INotificationSystem {
    void sendNotification(Long userId, String message, NotificationType type);

    void notifyEventUpdate(Long eventId, String message);

    void sendBulkNotification(List<Long> userIds, String message);

    // Additional methods for retrieving notifications
    List<Notification> getUserNotifications(String userEmail);

    List<Notification> getUnreadNotifications(String userEmail);

    void markAsRead(Long notificationId);
}
