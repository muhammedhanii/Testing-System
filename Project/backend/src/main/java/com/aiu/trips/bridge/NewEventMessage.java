package com.aiu.trips.bridge;

import java.time.LocalDateTime;

/**
 * NewEventMessage as per Notification.pu diagram
 * Bridge Pattern - New event notification message
 */
public class NewEventMessage extends NotificationMessage {

    private String eventName;
    private LocalDateTime eventDate;

    public NewEventMessage(NotificationChannel channel, String eventName, LocalDateTime eventDate) {
        super(channel);
        this.eventName = eventName;
        this.eventDate = eventDate;
    }

    @Override
    protected String formatContent() {
        return String.format("New Event: %s scheduled for %s. Register now!", eventName, eventDate);
    }
}
