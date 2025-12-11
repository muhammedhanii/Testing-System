package com.aiu.trips.adapter;

/**
 * IEmailService interface as per Notification.pu diagram
 * Target interface for email adapter
 */
public interface IEmailService {
    void sendEmail(String to, String subject, String body);
}
