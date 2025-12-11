package com.aiu.trips.bridge;

/**
 * NotificationMessage abstract class as per Notification.pu diagram
 * Bridge Pattern - Abstraction side (messages)
 */
public abstract class NotificationMessage {

    protected NotificationChannel channel;

    public NotificationMessage(NotificationChannel channel) {
        this.channel = channel;
    }

    public void send(String recipient) {
        String content = formatContent();
        channel.send(recipient, content);
    }

    protected abstract String formatContent();
}
