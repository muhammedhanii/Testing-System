package com.aiu.trips.bridge;

import com.aiu.trips.adapter.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * EmailChannel as per Notification.pu diagram
 * Bridge Pattern - Email channel implementation
 */
@Component
public class EmailChannel implements NotificationChannel {

    @Autowired
    private IEmailService emailService;

    @Override
    public void send(String recipient, String content) {
        emailService.sendEmail(recipient, "AIU Notification", content);
    }
}
