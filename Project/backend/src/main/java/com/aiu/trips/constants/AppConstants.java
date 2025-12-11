package com.aiu.trips.constants;

public final class AppConstants {
    
    private AppConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // Error Messages
    public static final String EVENT_NOT_FOUND = "Event not found with id: ";
    public static final String USER_NOT_FOUND = "User not found with id: ";
    public static final String BOOKING_NOT_FOUND = "Booking not found with id: ";
    public static final String FEEDBACK_NOT_FOUND = "Feedback not found with id: ";
    public static final String NOTIFICATION_NOT_FOUND = "Notification not found with id: ";
    
    // Booking Messages
    public static final String NO_SEATS_AVAILABLE = "No seats available for this event";
    public static final String EVENT_ALREADY_STARTED = "Cannot book an event that has already started";
    public static final String BOOKING_ALREADY_CANCELLED = "Booking is already cancelled";
    public static final String BOOKING_ALREADY_VALIDATED = "Ticket has already been validated";
    public static final String CANNOT_VALIDATE_CANCELLED = "Cannot validate a cancelled booking";
    
    // Feedback Messages
    public static final String FEEDBACK_ALREADY_EXISTS = "You have already submitted feedback for this event";
    public static final String MUST_ATTEND_TO_FEEDBACK = "You must attend the event before submitting feedback";
    public static final String INVALID_RATING = "Rating must be between 1 and 5";
    
    // Success Messages
    public static final String BOOKING_CREATED = "Booking created successfully";
    public static final String BOOKING_CANCELLED = "Booking cancelled successfully";
    public static final String BOOKING_VALIDATED = "Ticket validated successfully";
    public static final String FEEDBACK_SUBMITTED = "Feedback submitted successfully";
    public static final String EVENT_CREATED = "Event created successfully";
    public static final String EVENT_UPDATED = "Event updated successfully";
    public static final String EVENT_DELETED = "Event deleted successfully";
    
    // Validation
    public static final int MIN_RATING = 1;
    public static final int MAX_RATING = 5;
    public static final int MAX_COMMENT_LENGTH = 1000;
    public static final int MAX_DESCRIPTION_LENGTH = 1000;
}
