package com.aiu.trips.bridge;

/**
 * EventUpdateMessage as per Notification.pu diagram
 * Bridge Pattern - Event update notification message
 */
public class EventUpdateMessage extends NotificationMessage {

    private String eventName;
    private String updateDetails;

    public EventUpdateMessage(NotificationChannel channel, String eventName, String updateDetails) {
        super(channel);
        this.eventName = eventName;
        this.updateDetails = updateDetails;
    }

    @Override
    protected String formatContent() {
        return String.format("Event Update for %s: %s", eventName, updateDetails);
    }
}
