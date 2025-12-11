package com.aiu.trips.bridge;

/**
 * ReminderMessage as per Notification.pu diagram
 * Bridge Pattern - Reminder notification message
 */
public class ReminderMessage extends NotificationMessage {

    private String eventName;
    private Integer hoursUntilEvent;

    public ReminderMessage(NotificationChannel channel, String eventName, Integer hoursUntilEvent) {
        super(channel);
        this.eventName = eventName;
        this.hoursUntilEvent = hoursUntilEvent;
    }

    @Override
    protected String formatContent() {
        return String.format("Reminder: %s starts in %d hours. Don't forget!", eventName, hoursUntilEvent);
    }
}
