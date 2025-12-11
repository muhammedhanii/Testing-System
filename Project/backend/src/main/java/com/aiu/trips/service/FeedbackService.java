package com.aiu.trips.service;

import com.aiu.trips.constants.AppConstants;
import com.aiu.trips.enums.BookingStatus;
import com.aiu.trips.exception.ResourceNotFoundException;
import com.aiu.trips.exception.ValidationException;
import com.aiu.trips.model.Booking;
import com.aiu.trips.model.Event;
import com.aiu.trips.model.Feedback;
import com.aiu.trips.model.User;
import com.aiu.trips.repository.BookingRepository;
import com.aiu.trips.repository.EventRepository;
import com.aiu.trips.repository.FeedbackRepository;
import com.aiu.trips.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public Feedback createFeedback(Long eventId, Integer rating, String comment, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userEmail));
        
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new ResourceNotFoundException(AppConstants.EVENT_NOT_FOUND + eventId));

        // Check if user has attended the event
        Booking booking = bookingRepository.findByUser_IdAndEvent_Id(user.getId(), eventId)
            .stream()
            .filter(b -> BookingStatus.ATTENDED.equals(b.getStatus()))
            .findFirst()
            .orElseThrow(() -> new ValidationException(AppConstants.MUST_ATTEND_TO_FEEDBACK));

        // Check if feedback already exists
        if (feedbackRepository.existsByUser_IdAndEvent_Id(user.getId(), eventId)) {
            throw new ValidationException(AppConstants.FEEDBACK_ALREADY_EXISTS);
        }

        // Validate rating
        if (rating < AppConstants.MIN_RATING || rating > AppConstants.MAX_RATING) {
            throw new ValidationException(AppConstants.INVALID_RATING);
        }

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setEvent(event);
        feedback.setRating(rating);
        feedback.setComment(comment);

        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getEventFeedbacks(Long eventId) {
        return feedbackRepository.findByEvent_Id(eventId);
    }

    public List<Feedback> getUserFeedbacks(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userEmail));
        return feedbackRepository.findByUser_Id(user.getId());
    }

    public Double getEventAverageRating(Long eventId) {
        List<Feedback> feedbacks = feedbackRepository.findByEvent_Id(eventId);
        if (feedbacks.isEmpty()) {
            return 0.0;
        }
        return feedbacks.stream()
            .mapToInt(Feedback::getRating)
            .average()
            .orElse(0.0);
    }
}
