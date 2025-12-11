package com.aiu.trips.bridge;

/**
 * NotificationChannel interface as per Notification.pu diagram
 * Bridge Pattern - Implementation side (channels)
 */
public interface NotificationChannel {
    void send(String recipient, String content);
}
