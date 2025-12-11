package com.aiu.trips.bridge;

import com.aiu.trips.factory.IModelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * InAppChannel as per Notification.pu diagram
 * Bridge Pattern - In-app notification channel implementation
 */
@Component
public class InAppChannel implements NotificationChannel {

    @Autowired
    private IModelFactory modelFactory;

    @Override
    public void send(String recipient, String content) {
        // Store notification in database via model factory
        // For now, just log it
        System.out.println("IN-APP notification to " + recipient + ": " + content);
    }
}
